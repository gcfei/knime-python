import os
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters

gateway = None


class EntryPoint(object):

    def entry(self):
        return os.getpid()

    class Java:
        implements = ["org.knime.python2.gateway.EntryPoint"]


if __name__ == "__main__":
    entry = EntryPoint()
    global gateway
    gateway = ClientServer(java_parameters=JavaParameters(),
                           python_parameters=PythonParameters(propagate_java_exceptions=True),
                           python_server_entry_point=entry)
