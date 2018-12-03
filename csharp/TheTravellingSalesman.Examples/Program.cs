using System;
using TheTravelingSalesman;

namespace TheTravellingSalesman.Examples
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello Travelling Salesman.");
            new CityTSM();

            if (args.Length > 0)
            {
                new CableRoutingTSM().Run(args[0]);
            }
            

            Console.WriteLine("Press ESC to close");
            do
            {
                while (!Console.KeyAvailable)
                {
                    // Do nothing
                }
            } while (Console.ReadKey(true).Key != ConsoleKey.Escape);
        }
    }
}
