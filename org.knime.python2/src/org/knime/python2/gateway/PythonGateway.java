/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Dec 18, 2019 (marcel): created
 */
package org.knime.python2.gateway;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;

import org.knime.python.typeextension.PythonModuleExtensions;
import org.knime.python2.Activator;
import org.knime.python2.DefaultPythonCommand;

import py4j.ClientServer;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 */
public class PythonGateway implements AutoCloseable {

    private static boolean shutDown = false;

    private static PythonGateway instance;

    public static synchronized PythonGateway getInstance() {
        if (instance == null) {
            if (!shutDown) {
                instance = new PythonGateway();
            } else {
                throw new IllegalStateException("Python gateway has been shut down and cannot be recreated.");
            }
        }
        return instance;
    }

    public static synchronized void shutdown() {
        if (instance != null) {
            instance.close();
            instance = null;
            shutDown = true;
        }
    }

    private ClientServer m_clientServer;

    private Process m_process;

    private Integer m_pid;

    private PythonGateway() {
        final ProcessBuilder pb =
            new DefaultPythonCommand("/home/marcel/python-configs/knime_nodes.sh").createProcessBuilder();
        final String gatewayModuleFilePath = "/home/marcel/git/knime-python/org.knime.python2/py/gateway/gateway.py";
        // Use the -u options to force Python to not buffer stdout and stderror.
        Collections.addAll(pb.command(), "-u", gatewayModuleFilePath);
        // Add all python modules to PYTHONPATH variable.
        String existingPath = pb.environment().get("PYTHONPATH");
        existingPath = existingPath == null ? "" : existingPath;
        String externalPythonPath = PythonModuleExtensions.getPythonPath();
        externalPythonPath += File.pathSeparator + Activator.getFile(Activator.PLUGIN_ID, "py").getAbsolutePath();
        if (!externalPythonPath.isEmpty()) {
            if (existingPath.isEmpty()) {
                existingPath = externalPythonPath;
            } else {
                existingPath = existingPath + File.pathSeparator + externalPythonPath;
            }
        }
        existingPath = existingPath + File.pathSeparator;
        pb.environment().put("PYTHONPATH", existingPath);

        // pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        // pb.redirectError(ProcessBuilder.Redirect.PIPE);
        try {
            m_process = pb.start();
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }

        try {
            Thread.sleep(5000);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }

        m_clientServer = new ClientServer(null);
        final EntryPoint entry = (EntryPoint)m_clientServer.getPythonServerEntryPoint(new Class[]{EntryPoint.class});
        m_pid = entry.entry();
        System.out.println("Python PID: " + m_pid);
    }

    @Override
    public void close() {
        if (m_clientServer != null) {
            m_clientServer.shutdown();
            // TODO: May require further cleanup. See: https://www.py4j.org/advanced_topics.html#py4j-memory-model
            m_clientServer = null;
        }
        // If the original process was a script, we have to kill the actual Python process by PID.
        if (m_pid != null) {
            try {
                ProcessBuilder pb;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    pb = new ProcessBuilder("taskkill", "/F", "/PID", "" + m_pid);
                } else {
                    pb = new ProcessBuilder("kill", "-KILL", "" + m_pid);
                }
                final Process p = pb.start();
                p.waitFor();
            } catch (final InterruptedException ex) {
                // Closing the kernel should not be interrupted.
                Thread.currentThread().interrupt();
            } catch (final Exception ignore) {
                // Ignore.
            }
        }
        if (m_process != null) {
            m_process.destroyForcibly();
            // TODO: Further action required in case the process cannot be destroyed via Java. See PythonKernel#close()
        }
    }
}
