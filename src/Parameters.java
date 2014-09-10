public class Parameters {
    public final Boolean V, W, C, E;

    Parameters(String[] args) {
        Boolean tempV = false, tempW = false, tempC = false, tempE = false;
        for (String s : args) {
            if (s.toLowerCase().equals("-v")){
                tempV = true;
            }
            if (s.toLowerCase().equals("-w")){
                tempW = true;
            }
            if (s.toLowerCase().equals("-c")){
                tempC = true;
            }
            if (s.toLowerCase().equals("-e")){
                tempE = true;
            }
        }
        V = tempV;
        W = tempW;
        C = tempC;
        E = tempE;
    }
}
