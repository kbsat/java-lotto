package lotto.domain;

import lotto.generator.NumberGenerator;
import lotto.generator.UserInputNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultsTest {
    private static final String BONUS_NUMBER = "7";
    private static final int RESULT_BASE = 6;
    NumberGenerator numberGenerator;
    List<LottoNumber> winningNumbers;
    LottoNumber bonusNumber;
    WinningLottoTicket winningLotto;
    List<LottoNumber> notWinningUserLottoNumbers;
    LottoTicket notWinningUserLottoTicket;
    List<LottoNumber> secondWinningUserLottoNumbers;
    LottoTicket secondWinningUserLottoTicket;

    @BeforeEach
    void init() {
        numberGenerator = new UserInputNumberGenerator();
        winningNumbers = numberGenerator.generateNumbers("1,2,3,4,5,6");
        bonusNumber = new LottoNumber(BONUS_NUMBER);
        winningLotto = new WinningLottoTicket(winningNumbers, bonusNumber);

        notWinningUserLottoNumbers = numberGenerator.generateNumbers("7,8,9,10,11,12");
        notWinningUserLottoTicket = new LottoTicket(notWinningUserLottoNumbers);

        secondWinningUserLottoNumbers = numberGenerator.generateNumbers("1,2,3,4,5,7");
        secondWinningUserLottoTicket = new LottoTicket(secondWinningUserLottoNumbers);
    }

    @Test
    @DisplayName("당첨되지 않았을 때")
    void calculateResultsTest_no_winning() {
        Results results = new Results(new LottoTickets(Arrays.asList(notWinningUserLottoTicket)), winningLotto);
        results.calculateResults();
        assertThat(results.getResults()
                .get(0 + RESULT_BASE)
                .getWinningInfo()
                .name())
                .isEqualTo(WinningInfo.FAIL.name());
    }

    @Test
    @DisplayName("당첨이_존재할_때")
    void calculateResultsTest_exist_winning() {
        Results results = new Results(new LottoTickets(Arrays.asList(notWinningUserLottoTicket, secondWinningUserLottoTicket)), winningLotto);
        results.calculateResults();
        assertThat(results.getResults()
                .get(0 + RESULT_BASE)
                .getWinningInfo()
                .name())
                .isEqualTo(WinningInfo.FAIL.name());
        assertThat(results.getResults()
                .get(1 + RESULT_BASE)
                .getWinningInfo()
                .name())
                .isEqualTo(WinningInfo.SECOND.name());
    }

    @Test
    @DisplayName("수익률 함수 테스트")
    void getEarningRateTest() {
        Results results = new Results(new LottoTickets(Arrays.asList(notWinningUserLottoTicket, secondWinningUserLottoTicket)), winningLotto);
        results.calculateResults();
        assertThat(results.getEarningRate())
                .isEqualTo(1500000);
    }

    @Test
    @DisplayName("로또 결과 테스트")
    void getSummaryTest() {
        Results results = new Results(new LottoTickets(Arrays.asList(notWinningUserLottoTicket, secondWinningUserLottoTicket)), winningLotto);

        Map<WinningInfo, Integer> manualSummary = new HashMap<>();
        for (WinningInfo winningInfo : WinningInfo.values()) {
            manualSummary.put(winningInfo, 1);
        }
        manualSummary.put(WinningInfo.SECOND, 2);
        manualSummary.remove(WinningInfo.FAIL);

        assertThat(results.getSummary()).isEqualTo(manualSummary);
    }
}
