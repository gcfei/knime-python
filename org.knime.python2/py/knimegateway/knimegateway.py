import os
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters

client_server = '<uninitialized>'


class EntryPoint(object):

    def getPid(self):
        return os.getpid()

    def getNodeModel(self):
        from knimenodes.MyStandardKnimeNode import MyStandardKnimeNode
        return MyStandardKnimeNode()

    class Java:
        implements = ["org.knime.python2.gateway.EntryPoint"]


if __name__ == "__main__":
    entry = EntryPoint()
    client_server = ClientServer(java_parameters=JavaParameters(),
                                 python_parameters=PythonParameters(propagate_java_exceptions=True),
                                 python_server_entry_point=entry)
