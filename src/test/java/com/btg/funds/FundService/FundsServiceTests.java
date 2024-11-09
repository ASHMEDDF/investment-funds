package com.btg.funds.FundService;

import com.btg.funds.model.Fund;
import com.btg.funds.repository.FundRepository;
import com.btg.funds.service.FundService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class FundsServiceTests {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundService fundService;

    public FundsServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuscribirFondo() {
        Fund fund = new Fund();
        fund.setCategory("category");
        fund.setName("name");
        fund.setMinAmount(1);
        fundService.subscribeFund(fund);
        verify(fundRepository).save(fund);
    }

    @Test
    public void testCancelarFondo() {
        fundService.cancelFund(1);
        verify(fundRepository).deleteById(1);
    }
}
