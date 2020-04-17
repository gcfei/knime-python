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
 *   Jan 25, 2019 (marcel): created
 */
package org.knime.python2.config.advanced;

import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.python2.config.PythonConfig;
import org.knime.python2.config.PythonConfigStorage;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class PythonKernelQueueConfig implements PythonConfig {

    // TODO: change keys, should be concise and expressive since user may configure the preference file manually

    public static final String CFG_KEY_MAX_NUMBER_OF_IDLING_PROCESSES = "maxNumberOfIdlingProcesses";

    public static final int DEFAULT_MAX_NUMBER_OF_IDLING_PROCESSES = 3;

    public static final String CFG_KEY_EXPIRATION_DURATION_IN_SECONDS = "expirationDuration";

    /**
     * Note: the duration until expired entries are actually evicted is generally longer than this value because the
     * underlying pool performs clean-ups in a timer-based manner. The clean-up interval of the timer is governed by
     * {@link #EVICTION_CHECK_INTERVAL_IN_MILLISECONDS}.
     */
    public static final int DEFAULT_EXPIRATION_DURATION_IN_SECONDS = 5 * 60;

    private final SettingsModelInteger m_maxNumberOfIdlingKernels =
        new SettingsModelInteger(CFG_KEY_MAX_NUMBER_OF_IDLING_PROCESSES, DEFAULT_MAX_NUMBER_OF_IDLING_PROCESSES);

    private final SettingsModelInteger m_expirationDurationInSeconds =
        new SettingsModelInteger(CFG_KEY_EXPIRATION_DURATION_IN_SECONDS, DEFAULT_EXPIRATION_DURATION_IN_SECONDS);

    public SettingsModelInteger getMaxNumberOfIdlingProcesses() {
        return m_maxNumberOfIdlingKernels;
    }

    public SettingsModelInteger getExpirationDurationInSeconds() {
        return m_expirationDurationInSeconds;
    }

    @Override
    public void saveConfigTo(final PythonConfigStorage storage) {
        storage.saveIntegerModel(m_maxNumberOfIdlingKernels);
        storage.saveIntegerModel(m_expirationDurationInSeconds);
    }

    @Override
    public void loadConfigFrom(final PythonConfigStorage storage) {
        storage.loadIntegerModel(m_maxNumberOfIdlingKernels);
        storage.loadIntegerModel(m_expirationDurationInSeconds);
    }
}
