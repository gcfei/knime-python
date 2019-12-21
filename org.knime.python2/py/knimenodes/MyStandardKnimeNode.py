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

from py4j.java_gateway import is_instance_of
from py4j.java_gateway import java_import

# Import KNIME classes:
from knimegateway.knimegateway import client_server as cs

knime = cs.new_jvm_view()
java_import(knime, 'java.io.File')
java_import(knime, 'org.knime.core.data.DoubleValue')
java_import(knime, 'org.knime.core.data.IntValue')
java_import(knime, 'org.knime.core.data.container.filter.TableFilter')
java_import(knime, 'org.knime.core.data.def.DoubleCell')
java_import(knime, 'org.knime.core.data.def.IntCell')
java_import(knime, 'org.knime.core.node.BufferedDataTable')
java_import(knime, 'org.knime.core.node.ExecutionContext')
java_import(knime, 'org.knime.core.node.ExecutionMonitor')
java_import(knime, 'org.knime.core.node.NodeSettingsRO')
java_import(knime, 'org.knime.core.node.NodeSettingsWO')
java_import(knime, 'org.knime.core.node.defaultnodesettings.SettingsModelInteger')
java_import(knime, 'org.knime.core.node.defaultnodesettings.SettingsModelString')
java_import(knime, 'org.knime.core.node.port.PortObject')
java_import(knime, 'org.knime.core.node.port.PortObjectSpec')
java_import(knime, 'org.knime.core.node.port.PortType')

# Type hints
from typing import Sequence

# TODO: Remove all the py4j-specific (and/or tedious Java-specific) stuff from user code.

_operators = cs.new_array(str, 4)
_operators[0] = '+'
_operators[1] = '-'
_operators[2] = '*'
_operators[3] = '/'


class MyStandardKnimeNode():

    @staticmethod
    def _create_operand1_settings() -> knime.SettingsModelString:
        return knime.SettingsModelString("first_operand", "")

    @staticmethod
    def _create_operand2_settings() -> knime.SettingsModelString:
        return knime.SettingsModelString("second_operand", "")

    @staticmethod
    def _create_operator_settings() -> knime.SettingsModelString:
        return knime.SettingsModelString("operator", _operators[0])

    @staticmethod
    def _create_constant_settings() -> knime.SettingsModelInteger:
        return knime.SettingsModelInteger("constant", 42)

    _operand1_settings = _create_operand1_settings()
    _operand2_settings = _create_operand2_settings()
    _operator_settings = _create_operator_settings()
    _constant_settings = _create_constant_settings()

    def __init__(self):
        pass

    def getInputPortTypes(self) -> Sequence[knime.PortType]:
        types = cs.new_array(knime.PortType, 2)
        types[0] = knime.BufferedDataTable.TYPE
        types[1] = knime.BufferedDataTable.TYPE
        return types

    def getOutputPortTypes(self) -> Sequence[knime.PortType]:
        types = cs.new_array(knime.PortType, 1)
        types[0] = knime.BufferedDataTable.TYPE
        return types

    def loadInternals(self, nodeInternDir: knime.File, exec: knime.ExecutionMonitor):
        # Nothing to do.
        pass

    def saveInternals(self, nodeInternDir: knime.File, exec: knime.ExecutionMonitor):
        # Nothing to do.
        pass

    def saveSettingsTo(self, settings: knime.NodeSettingsWO):
        self._operand1_settings.saveSettingsTo(settings)
        self._operand2_settings.saveSettingsTo(settings)
        self._operator_settings.saveSettingsTo(settings)
        self._constant_settings.saveSettingsTo(settings)

    def validateSettings(self, settings: knime.NodeSettingsRO):
        self._operand1_settings.validateSettings(settings)
        self._operand2_settings.validateSettings(settings)
        self._operator_settings.validateSettings(settings)
        self._constant_settings.validateSettings(settings)

    def loadValidatedSettingsFrom(self, settings: knime.NodeSettingsRO):
        self._operand1_settings.loadSettingsFrom(settings)
        self._operand2_settings.loadSettingsFrom(settings)
        self._operator_settings.loadSettingsFrom(settings)
        self._constant_settings.loadSettingsFrom(settings)

    def configure(self, inSpecs: Sequence[knime.PortObjectSpec]) -> Sequence[knime.PortObjectSpec]:
        table_spec1 = inSpecs[0]
        column_spec1 = table_spec1.getColumnSpec(self._operand1_settings.getStringValue())
        column1_type = column_spec1.getType()

        table_spec2 = inSpecs[1]
        column_spec2 = table_spec2.getColumnSpec(self._operand2_settings.getStringValue())
        column2_type = column_spec2.getType()

        if column1_type.isCompatible(knime.IntValue) and column2_type.isCompatible(knime.IntValue):
            if self._operator_settings.getStringValue() != "/":
                out_type = knime.IntCell.TYPE
            else:
                out_type = knime.DoubleCell.TYPE
        elif column1_type.isCompatible(knime.DoubleValue) and column2_type.isCompatible(knime.DoubleValue):
            out_type = knime.DoubleCell.TYPE
        else:
            # TODO: Translate to InvalidSettingsException?
            raise TypeError(
                "Operands must both be either of type 'Number (integer)' or of type 'Number (double)'.")
        return {'name': "Calculated value", 'type': out_type}

    def execute(self, inObjects: Sequence[knime.PortObject], exec: knime.ExecutionContext) -> Sequence[
        knime.PortObject]:
        table1 = inObjects[0]
        table_spec1 = table1.getDataTableSpec()
        table2 = inObjects[1]
        table_spec2 = table2.getDataTableSpec()

        if table1.size() != table2.size():
            raise ValueError("Input tables are not equal in size.")

        operand1_name = self._operand1_settings.getStringValue()
        column1_iterator = table1.iteratorWithFilter(
            knime.TableFilter.materializeCols(table_spec1, operand1_name))
        operand2_name = self._operand2_settings.getStringValue()
        column2_iterator = table2.iteratorWithFilter(
            knime.TableFilter.materializeCols(table_spec2, operand2_name))

        operator = self._operator_settings.getStringValue()
        constant = self._constant_settings.getIntValue()

        while column1_iterator.hasNext():
            operand1_cell = column1_iterator.next()
            operand2_cell = column2_iterator.next()

            if is_instance_of(cs, operand1_cell, knime.IntValue):
                operand1 = operand1_cell.getIntValue()
            else:
                operand1 = operand1_cell.getDoubleValue()

            if is_instance_of(cs, operand2_cell, knime.IntValue):
                operand2 = operand2_cell.getIntValue()
            else:
                operand2 = operand2_cell.getDoubleValue()

            if operator == '+':
                return operand1 + operand2 + constant
            elif operator == '-':
                return operand1 - operand2 - constant
            elif operator == '*':
                return operand1 * operand2 * constant
            elif operator == '/':
                return operand1 / operand2 / constant
            else:
                raise ValueError('Unknown operator: ' + operator)

    def reset(self):
        # Nothing to do.
        pass

    class Java:
        implements = ["org.knime.python2.knimenodes.PythonNodeModel"]
