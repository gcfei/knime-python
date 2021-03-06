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
 *   May 24, 2018 (marcel): created
 */
package org.knime.python2.kernel;

import java.util.Optional;

import org.knime.python2.PythonFrameSummary;

/**
 * Deriving classes that have a {@link #getFormattedPythonTraceback() Python traceback} available, should also override
 * {@link Throwable#printStackTrace(java.io.PrintStream)} and {@link Throwable#printStackTrace(java.io.PrintWriter)} to
 * include it in their printed representation, therefore improving logging and simplifying debugging.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public interface PythonException {

    /**
     * Returns the detail message string of this exception.
     *
     * @return the detail message string of this {@link PythonException} instance. Is neither null nor empty and
     *         suitable for presentation to the user.
     * @see Throwable#getMessage()
     */
    String getMessage();

    /**
     * @return The formatted string representation of the trace back of the underlying problem on Python side, if any.
     */
    default Optional<String> getFormattedPythonTraceback() {
        return Optional.empty();
    }

    /**
     * @return The individual frames of the trace back of the underlying problem on Python side, if any.
     */
    default Optional<PythonFrameSummary[]> getPythonTraceback() {
        return Optional.empty();
    }

    /**
     * @return a short exception message (without a Traceback), if any.
     */
    default Optional<String> getShortMessage() {
        return Optional.empty();
    }
}
