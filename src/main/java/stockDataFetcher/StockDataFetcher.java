package stockDataFetcher;

/*
uses web scrapping python yfinance api to grab stock data.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StockDataFetcher {
    private Map stockDataResults;
    private static final String signOfResult="END_RESULTS";
    public StockDataFetcher(String tickersList) {

        try {
            Process pythonExec = Runtime.getRuntime().exec(buildPythonExecCommand(tickersList));
            System.out.println("\n###########START_OF_PYTHON_SCRIPT############");
            handleResults(pythonExec.getInputStream());
            handleError(pythonExec.getErrorStream());
            System.out.println("############END_OF_PYTHON_SCRIPT############");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleError(InputStream errorInputStream) {
        String errString = fromInputStreamToString(errorInputStream);
        if (!errString.equals("")) System.out.println("error of python script:\n" + errString);
    }

    private void handleResults(InputStream resultsInputStream) {
        String tempResultStr = fromInputStreamToString(resultsInputStream);
        if (!tempResultStr.equals("")) {
            System.out.println("results of python script:\n" + tempResultStr);
            String resultString = tempResultStr.split(signOfResult)[1];
            setStockDataResultsMap(resultString);
        }
    }

    private String buildPythonExecCommand(String tickersList) {
        //String pythonPath = "C:\\Users\\נתנאל\\AppData\\Local\\Programs\\Python\\Python38-32\\python.exe";
        //String scriptPath = "C:\\Users\\נתנאל\\PycharmProjects\\stockDataFetcher\\stockDataFetch\\getStockData.py";
        String pythonPath="python";
        String localDir = System.getProperty("user.dir");
        String candidateStocksPath=localDir + "\\src\\main\\java\\scripts\\getStockData.py";
        return pythonPath + " " + candidateStocksPath + tickersList;
    }

    private String fromInputStreamToString(InputStream inputStream) {
        String result = "";
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(inputStream));
        String outPutString = null;
        while (true) {
            try {
                if ((outPutString = stdInput.readLine()) == null) break;
            } catch (IOException e) {
                System.out.println("error, could not read python output.");
                e.printStackTrace();
            }
            result += outPutString + '\n';
        }
        return result;
    }

    private void setStockDataResultsMap(String resultString) {
        String pairDelimiter=",";
        String insidePairDelimiter=":";
        if (resultString!=null){
            String cleanResultString=resultString.substring(resultString.indexOf('{')+1,resultString.indexOf('}'));
            stockDataResults= Arrays.stream(cleanResultString.split(pairDelimiter))
                    .map(s -> s.split(insidePairDelimiter))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        }
    }

    public double getStockPrice(String stockSymbol){
        String currentStockPrice;
        if (stockDataResults == null) System.out.println("error: Map stockDataResults is null");
        String symbolInMap = " '" + stockSymbol + "'";
        currentStockPrice = (String) stockDataResults.get(symbolInMap);
        if (currentStockPrice == null) {
            symbolInMap = "'" + stockSymbol + "'";
            currentStockPrice = (String) stockDataResults.get(symbolInMap);
        }
        return Double.parseDouble(currentStockPrice);
    }
}
