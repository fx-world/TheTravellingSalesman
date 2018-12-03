using OfficeOpenXml;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using TheTravelingSalesman;
using TheTravelingSalesman.Solvers;

namespace TheTravellingSalesman.Examples
{
    class CableRoutingTSM
    {
        public void Run(string filename)
        {
            Dictionary<string, Pin> startPins = new Dictionary<string, Pin>();
            Dictionary<string, List<Pin>> pinMap = new Dictionary<string, List<Pin>>();

            FileInfo file = new FileInfo(filename);
            using (ExcelPackage excel = new ExcelPackage(file))
            {
                //foreach (ExcelWorksheet worksheet in excel.Workbook.Worksheets.)
                ExcelWorksheet worksheet = excel.Workbook.Worksheets["Datensatz1"];
                {
                    for (int l = 2; l <= worksheet.Dimension.End.Row; l++)
                    {
                        double xPos = GetValue(worksheet.Cells[l, 1].Value);
                        double yPos = GetValue(worksheet.Cells[l, 2].Value);
                        double zPos = GetValue(worksheet.Cells[l, 3].Value);

                        string pinType = (string)worksheet.Cells[l, 4].Value;
                        string circuit = (string)worksheet.Cells[l, 5].Value;

                        Pin pin = new Pin(pinType, circuit, xPos, yPos, zPos);

                        Match match = Regex.Match(circuit, "(.*) (F\\d+)", RegexOptions.IgnoreCase);
                        if (match.Success) 
                        {
                            string parentCircuit = match.Groups[1].Value;
                            List<Pin> pins = null;

                            if (pinMap.ContainsKey(circuit))
                            {
                                pins = pinMap[circuit];
                            }
                            else 
                            {
                                pins = new List<Pin>();
                                pinMap.Add(circuit, pins);
                            }
                            pins.Add(pin);
                        }
                        else
                        {
                            startPins.Add(circuit, pin);
                        }

                        Debug.WriteLine(circuit + " - " + xPos + ":" + yPos + ":" + zPos);
                    }
                }
            }

            foreach (KeyValuePair<string, List<Pin>> entry in pinMap)
            {
                string circuit = entry.Key;
                List<Pin> pins = entry.Value;
                Pin startPin = null;

                Match match = Regex.Match(circuit, "(.*) (F\\d+)", RegexOptions.IgnoreCase);
                if (match.Success)
                {
                    string key = match.Groups[1].Value;
                    startPin = startPins[match.Groups[1].Value];
                }

                IProblem<Pin> problem = new ProblemBuilder<Pin>(typeof(Pin))
                        .Locations(pins)
                        .Distances((Pin p1, Pin p2) => CalcDistance(p1, p2))
                        .FixedFirstLocation(startPin)
                        .BuildIntegerArray();

                ISolver<Pin> solverNN = new NearestNeighborSolver<Pin>(problem);
                ISolver<Pin> solverPBB = new ParallelBranchBoundSolver<Pin>(problem);
                Stopwatch stopWatch = new Stopwatch();
                
                System.Console.Out.WriteLine("Circuit: " + circuit);
                System.Console.Out.WriteLine("\tNN = " + solverNN.Solve());
                stopWatch.Start();
                System.Console.Out.WriteLine("\tPBB= " + solverPBB.Solve());
                stopWatch.Stop();
                System.Console.Out.WriteLine("\t\t time = " + stopWatch.Elapsed);
            }
        }

        public double GetValue(object input)
        {
            double result = 0;
            if (input != null)
            {
                switch (input)
                {
                    case double d: result = d; break;
                    case string s: double.TryParse(s, out result); break;
                    case null: result = 0; break; 
                }
            }

            return result;
        }

        public double CalcDistance(Pin a, Pin b)
        {
            return Math.Abs(a.x - b.x) + Math.Abs(a.y - b.y) + Math.Abs(a.z - b.z);
        }
    }

    class Pin
    {
        internal string pinType;
        internal string circuit;
        internal double x;
        internal double y;
        internal double z;

        public Pin(string pinType, string circuit, double x, double y, double z)
        {
            this.pinType = pinType;
            this.circuit = circuit;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public override string ToString() => pinType;
    }
}
