# -*- coding: utf-8 -*-
# ------------------------------------------------------------------------
#  Copyright by KNIME AG, Zurich, Switzerland
#  Website: http://www.knime.com; Email: contact@knime.com
#
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License, Version 3, as
#  published by the Free Software Foundation.
#
#  This program is distributed in the hope that it will be useful, but
#  WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, see <http://www.gnu.org/licenses>.
#
#  Additional permission under GNU GPL version 3 section 7:
#
#  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
#  Hence, KNIME and ECLIPSE are both independent programs and are not
#  derived from each other. Should, however, the interpretation of the
#  GNU GPL Version 3 ("License") under any applicable laws result in
#  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
#  you the additional permission to use and propagate KNIME together with
#  ECLIPSE with only the license terms in place for ECLIPSE applying to
#  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
#  license terms of ECLIPSE themselves allow for the respective use and
#  propagation of ECLIPSE together with KNIME.
#
#  Additional permission relating to nodes for KNIME that extend the Node
#  Extension (and in particular that are based on subclasses of NodeModel,
#  NodeDialog, and NodeView) and that only interoperate with KNIME through
#  standard APIs ("Nodes"):
#  Nodes are deemed to be separate and independent programs and to not be
#  covered works.  Notwithstanding anything to the contrary in the
#  License, the License does not apply to Nodes, you are not required to
#  license Nodes under the License, and you are granted a license to
#  prepare and propagate Nodes, in each case even if such Nodes are
#  propagated with or for interoperation with KNIME.  The owner of a Node
#  may freely choose the license terms applicable to such Node, including
#  when such Node is propagated with or for interoperation with KNIME.
# ------------------------------------------------------------------------
"""
@author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
"""

import numpy as np
from pandas import DataFrame

from py4j.java_gateway import get_field

# Import KNIME classes:
from gateway import global_gateway as gg
from py4j.java_gateway import java_import

knime = gg.new_jvm_view()
java_import(knime, 'java.io.File')
java_import(knime, 'org.knime.core.data.def.DoubleCell')
java_import(knime, 'org.knime.core.data.def.IntCell')
java_import(knime, 'org.knime.core.node.BufferedDataTable')
java_import(knime, 'org.knime.core.node.ExecutionContext')
java_import(knime, 'org.knime.core.node.ExecutionMonitor')
java_import(knime, 'org.knime.core.node.NodeSettingsRO')
java_import(knime, 'org.knime.core.node.NodeSettingsWO')
java_import(knime, 'org.knime.core.node.port.PortObject')
java_import(knime, 'org.knime.core.node.port.PortObjectSpec')
java_import(knime, 'org.knime.core.node.port.PortType')

# Type hints
from typing import Sequence


# TODO: Remove all the py4j-specific (and/or tedious Java-specific) stuff from user code.


class MyStandardPythonNode():

    # TODO: Add settings models

    def getInputPortTypes(self) -> Sequence[knime.PortType]:
        types = gg.new_array(knime.PortType, 2)
        types[0] = knime.BufferedDataTable.TYPE
        types[1] = knime.BufferedDataTable.TYPE
        return types

    def getOutputPortTypes(self) -> Sequence[knime.PortType]:
        types = gg.new_array(knime.PortType, 1)
        types[0] = knime.BufferedDataTable.TYPE
        return types

    def loadInternals(self, nodeInternDir: knime.File, exec: knime.ExecutionMonitor):
        # Nothing to do.
        pass

    def saveInternals(self, nodeInternDir: knime.File, exec: knime.ExecutionMonitor):
        # Nothing to do.
        pass

    def saveSettingsTo(self, settings: knime.NodeSettingsWO):
        # TODO: Implement
        pass

    def validateSettings(self, settings: knime.NodeSettingsRO):
        # TODO: Implement
        pass

    def loadValidatedSettingsFrom(self, settings: knime.NodeSettingsRO):
        # TODO: Implement
        pass

    def configure(self, inSpecs: Sequence[knime.PortObjectSpec]) -> Sequence[knime.PortObjectSpec]:
        table_spec1 = inSpecs[0]
        column_spec1 = table_spec1.getColumnSpec(table1Column.getStringValue())
        column1_type = column_spec1.getType()

        table_spec2 = inSpecs[1]
        column_spec2 = table_spec2.getColumnSpec(table2Column.getStringValue())
        column2_type = column_spec2.getType()

        # TODO: Fill in DataValue arguments
        if column1_type.isCompatible() and column2_type.isCompatible():
            if operator.getStringValue() != "/":
                out_type = knime.IntCell.TYPE
            else:
                out_type = knime.DoubleCell.TYPE
        elif column1_type.isCompatible() and column2_type.isCompatible():
            out_type = knime.DoubleCell.TYPE
        else:
            # TODO: Translate to InvalidSettingsException?
            raise TypeError(
                "Input columns must both be either of type 'Number (integer)' or of type 'Number (double)'.")
        return {'name': "Calculated value", 'type': out_type}

    def execute(self, inObjects: Sequence[knime.PortObject], exec: knime.ExecutionContext) -> Sequence[
        knime.PortObject]:
        table1 = inObjects[0]
        table2 = inObjects[1]

        # TODO:
        #  table1.iteratorWithFilter()
        #  table2.iteratorWithFilter()

        if self.operation == '+':
            return number1 + number2 / self.constant
        elif self.operation == '-':
            return number1 - number2 / self.constant
        elif self.operation == '*':
            return number1 * number2 / self.constant
        elif self.operation == '/':
            return number1 / number2 / self.constant
        else:
            raise ValueError('Unknown operation: ' + self.operation)

    def reset(self):
        # Nothing to do.
        pass

    class Java:
        implements = ["org.knime.python2.knimenodes.PythonNodeModel"]
