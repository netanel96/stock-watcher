import candidateStocks.ICandidateStocksFetcher;
import candidateStocks.JsonFileCandidateStocksFetcher;
import entities.EStatus;
import entities.Stock;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.Assert.assertEquals;

public class jsonUpdateTest {

    @Test
    public void testUpdateJson() throws FileNotFoundException, InterruptedException {
        ICandidateStocksFetcher jsonFileCandidateStocksFetcher=new JsonFileCandidateStocksFetcher();
        int oldCycles= Integer.parseInt(jsonFileCandidateStocksFetcher.getCandidateStocks().get(0).getCycles());
        System.out.println("oldCYCLES:"+oldCycles);
        Stock stock=jsonFileCandidateStocksFetcher.getCandidateStocks().get(0);
        System.out.println("stock before change:"+stock);
        stock.setCycles(String.valueOf(Integer.parseInt(stock.getCycles())+1));
        jsonFileCandidateStocksFetcher.UpdateStock(stock);
        ICandidateStocksFetcher newJsonFileCandidateStocksFetcher=new JsonFileCandidateStocksFetcher();
        int newCycles= Integer.parseInt(newJsonFileCandidateStocksFetcher.getCandidateStocks().get(0).getCycles());
        System.out.println("newCycles"+newCycles);
        assertEquals(oldCycles,newCycles-1);



    }
}
