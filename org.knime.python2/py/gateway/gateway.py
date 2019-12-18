from py4j.clientserver import ClientServer, JavaParameters, PythonParameters


class EntryPoint(object):

    def entry(self):
        raise ValueError("works!")

    class Java:
        implements = ["org.knime.python2.gateway.PythonGateway$EntryPoint"]


if __name__ == "__main__":
    entry = EntryPoint()
    gateway = ClientServer(java_parameters=JavaParameters(),
                           python_parameters=PythonParameters(),
                           python_server_entry_point=entry)
