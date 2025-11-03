# 🔨 구현 체크리스트

## 📐 설계 원칙

### YAGNI (You Aren't Gonna Need It)
- [x] ✅ Lottery 인터페이스 만들지 않기 (현재 요구사항에 로또만 있음)
- [x] ✅ Policy, Validator 등 불필요한 추상화 제거
- [x] ✅ 각 도메인 객체가 자신의 검증을 직접 책임

### Tell, Don't Ask
- [x] ✅ getter 최소화, 비즈니스 메서드 제공
- [x] ✅ `countMatches()`, `contains()` 등 행동 중심 메서드

---

## 🏗️ 도메인 계층

### Lotto 클래스
- [x] `private final List<Integer> numbers` 필드
- [x] 생성자에서 검증 수행 (6개, 1~45, 중복 없음)
- [x] `countMatches(List<Integer>)` - 당첨 번호와 일치 개수
- [x] `contains(int)` - 보너스 번호 포함 여부
- [x] `getNumbers()` - 방어적 복사로 불변성 보장

### WinningNumbers 클래스
- [x] Lotto + 보너스 번호 관리
- [x] 생성자에서 보너스 검증 (1~45, 당첨 번호와 중복 안됨)
- [x] `match(Lotto)` - Rank 판정

### Rank Enum
- [x] 1등~5등, 낙첨 정의
- [x] 일치 개수, 보너스 일치 여부, 상금 관리
- [x] `of(int, boolean)` - 등수 판정

### LottoResult 클래스
- [x] 등수별 당첨 개수 집계
- [x] `addRank(Rank)` - 등수 추가
- [x] `countOf(Rank)` - 특정 등수 개수
- [x] `calculateTotalPrize()` - 총 상금
- [x] `calculateProfitRate(int)` - 수익률

### LottoGenerator 클래스
- [x] `generate(int)` - count만큼 로또 생성
- [x] Randoms.pickUniqueNumbersInRange() 사용

---

## 🎮 게임 실행 계층

### GameEngine (미구현)
- [ ] 전체 게임 플로우 제어
- [ ] 재입력 처리 (RetryExecutor 패턴)

---

## 🖥️ View 계층

### InputView
- [x] `readPurchaseAmount()` - 구입 금액 입력
- [x] `readWinningNumbers()` - 당첨 번호 입력
- [x] `readBonusNumber()` - 보너스 번호 입력

### OutputView
- [x] `printTicketPurchase(TicketPurchaseDTO)` - 구매 로또 출력
- [x] `printWinningStatistics(WinningStatisticsDTO)` - 당첨 통계 출력
- [x] `printError(String)` - 에러 메시지 출력

### DTO
- [x] TicketPurchaseDTO - 구매 로또 정보
- [x] WinningStatisticsDTO - 당첨 통계 정보
- [x] RankStatistic - 등수별 통계

---

## ⚠️ 예외 처리

### Domain Exception
- [x] InvalidLottoNumberException - 로또 번호 검증 실패
- [x] InvalidBonusNumberException - 보너스 번호 검증 실패
- [x] InvalidPurchaseAmountException - 구입 금액 검증 실패

### ErrorMessage Enum
- [x] 모든 에러 메시지 템플릿 정의

---

## 📝 남은 작업

### 입력 처리
- [ ] 구입 금액 입력 및 검증 로직
- [ ] 당첨 번호 입력 및 파싱 로직
- [ ] 보너스 번호 입력 및 검증 로직

### 게임 흐름
- [ ] 전체 게임 실행 엔진 구현
- [ ] 재입력 처리 로직
- [ ] Application 메인 메서드 연결

### 출력 처리
- [ ] DTO 변환 로직 구현
- [ ] 당첨 통계 포맷팅

### 테스트
- [ ] 도메인 단위 테스트
- [ ] 통합 테스트

---

## ✅ 완료 기준

- [ ] README의 모든 기능 요구사항 구현
- [ ] 프로그래밍 요구사항 준수 (indent 2, 메서드 15줄, else 금지, Enum 활용)
- [ ] 실행 결과가 예시와 일치
