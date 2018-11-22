using System;
using System.Collections.Generic;
using System.Text;

namespace java
{
    namespace io
    {
        class FileReader
        {
            private string path;

            public FileReader(string path)
            {
                this.path = path;
            }
        }

        class BufferedReader
        {
            private FileReader fr;

            public BufferedReader(FileReader fr)
            {
                this.fr = fr;
            }
        }
    }    
}
