package lotto.domain;

import lotto.domain.number.LottoNumber;
import lotto.domain.number.LottoNumberFactory;

public class WinningLottoTicket {
    private final LottoTicket winningTicket;
    private final LottoNumber bonusNumber;

    public WinningLottoTicket(LottoTicket winningTicket, int bonusNumber) {
        this.winningTicket = winningTicket;
        validateDuplicatedBonusNumber(bonusNumber);
        this.bonusNumber = LottoNumberFactory.getInstance(bonusNumber);
    }

    private void validateDuplicatedBonusNumber(int bonusNumber) {
        LottoNumber bonusLottoNumber = LottoNumberFactory.getInstance(bonusNumber);

        boolean isDuplicated = this.winningTicket.getLottoNumbers().stream()
            .anyMatch(winningNumber -> winningNumber.equals(bonusLottoNumber));

        if (isDuplicated) {
            throw new IllegalArgumentException(
                String.format("보너스 번호가 당첨 번호와 중복되었습니다. 중복된 번호 : %d", bonusNumber)
            );
        }
    }

    public Prize calculatePrize(LottoTicket lottoTicket) {
        int winningNumberCount = lottoTicket.compareWinningNumber(winningTicket);
        boolean isBonus = lottoTicket.containsAnyNumber(bonusNumber);

        return Prize.findByMatchCount(winningNumberCount, isBonus);
    }
}
