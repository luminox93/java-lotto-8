# 🔨 구현 체크리스트

## 📐 설계 원칙

### 확장 가능한 설계
- [x] Game 인터페이스 도입
- [x] GameEngine 중재자 패턴
- [x] 게임별 패키지 분리 (classicLotto/)
- [x] Domain-View 격리 (DTO)

### Tell, Don't Ask
- [x] getter 최소화
- [x] 행동 중심 메서드 설계
- [x] 도메인 객체가 검증 책임

---

## 🏗️ 도메인 계층

### Lotto 클래스

#### 필드
- [x] `List<Integer> numbers` (final)

#### 생성자 검증
- [x] 6개 검증
- [x] 1~45 범위 검증
- [x] 중복 검증

#### 메서드
- [x] `countMatches()` - 일치 개수 반환
- [x] `contains()` - 번호 포함 여부
- [x] `getNumbers()` - 방어적 복사

---

### WinningNumbers 클래스

#### 필드
- [x] `List<Integer> winningNumbers` (final)
- [x] `int bonusNumber` (final)

#### 생성자 검증
- [x] 당첨 번호 6개 검증 (Lotto 위임)
- [x] 보너스 범위 검증 (1~45)
- [x] 보너스 중복 검증

#### 메서드
- [x] `match(Lotto)` - 등수 판정

---

### Rank Enum

#### 상수 정의
- [x] FIRST (6개, 20억)
- [x] SECOND (5개+보너스, 3천만)
- [x] THIRD (5개, 150만)
- [x] FOURTH (4개, 5만)
- [x] FIFTH (3개, 5천)
- [x] NONE (낙첨)

#### 필드
- [x] `int matchCount` (final)
- [x] `boolean requireBonus` (final)
- [x] `int prize` (final)

#### 메서드
- [x] `static of(int, boolean)` - 등수 판정
- [x] `static getWinningRanks()` - 당첨 등수만 반환
- [x] `getMatchCount()`
- [x] `isRequireBonus()`
- [x] `getPrize()`

---

### LottoResult 클래스

#### 필드
- [x] `Map<Rank, Integer> rankCounts` (final)

#### 메서드
- [x] `addRank(Rank)` - 등수 집계
- [x] `countOf(Rank)` - 등수별 개수 반환
- [x] `calculateTotalPrize()` - 총 상금
- [x] `calculateProfitRate(int)` - 수익률

---

### LottoGenerator 클래스

#### 상수
- [x] `LOTTO_MIN_NUMBER = 1`
- [x] `LOTTO_MAX_NUMBER = 45`
- [x] `LOTTO_NUMBER_COUNT = 6`

#### 메서드
- [x] `generate(int)` - 지정 개수만큼 생성
- [x] `generateSingle()` - 로또 1장 생성

---

## 🎮 게임 실행 계층

### Game 인터페이스
- [x] `play()` 추상 메서드

---

### GameEngine 클래스

#### 필드
- [x] `Game game` (final)

#### 메서드
- [x] `run()` - 게임 실행

---

### LottoGame 클래스

#### 상수
- [x] `LOTTO_PRICE = 1000`

#### 필드
- [x] `InputView inputView` (final)
- [x] `OutputView outputView` (final)
- [x] `InputParser inputParser` (final)
- [x] `LottoGenerator lottoGenerator` (final)

#### 게임 플로우
- [x] 구입 금액 입력
- [x] 로또 발행
- [x] 발행 로또 출력
- [x] 당첨 번호 입력
- [x] 당첨 확인
- [x] 결과 출력

#### 입력 메서드
- [x] `readPurchaseAmount()`
- [x] `readWinningNumbersList()`
- [x] `readBonusNumber()`

#### 로또 생성 메서드
- [x] `generateLottos(int)`

#### 출력 메서드
- [x] `printLottos(List<Lotto>)`
- [x] `printResult(LottoResult, int)`

#### 당첨 확인 메서드
- [x] `checkWinning(List<Lotto>, WinningNumbers)`

#### 유틸리티 메서드
- [x] `repeatUntilValid(Supplier<T>)` - 재입력 처리
- [x] `createRankStatistics(LottoResult)` - 통계 생성
- [x] `formatRankDescription(Rank)` - 등수 포맷팅

---

### InputParser 클래스

#### 상수
- [x] `WINNING_NUMBER_DELIMITER = ","`
- [x] `MIN_PURCHASE_AMOUNT = 1000`
- [x] `PURCHASE_AMOUNT_UNIT = 1000`

#### 메서드
- [x] `parsePurchaseAmount(String)`
  - [x] 숫자 형식 검증
  - [x] 양수 검증
  - [x] 1,000원 단위 검증
  - [x] 최소 금액 검증
- [x] `parseWinningNumbers(String)`
  - [x] 쉼표 분리
  - [x] 공백 trim
  - [x] Integer 변환
- [x] `parseBonusNumber(String)`
  - [x] 공백 trim
  - [x] Integer 변환

---

## 🖥️ View 계층

### InputView 클래스
- [x] `readPurchaseAmount()` - 구입 금액 입력
- [x] `readWinningNumbers()` - 당첨 번호 입력
- [x] `readBonusNumber()` - 보너스 번호 입력

---

### OutputView 클래스
- [x] `printTicketPurchase(TicketPurchaseDTO)` - 구매 로또 출력
- [x] `printWinningStatistics(WinningStatisticsDTO)` - 통계 출력
- [x] `printError(String)` - 에러 출력

---

### DTO

#### TicketPurchaseDTO
- [x] `int count`
- [x] `List<List<Integer>> tickets`

#### WinningStatisticsDTO
- [x] `List<RankStatistic> rankStatistics`
- [x] `double profitRate`

#### RankStatistic
- [x] `String description`
- [x] `int count`

---

### 메시지 관리

#### InputMessage Enum
- [x] PURCHASE_AMOUNT
- [x] WINNING_NUMBERS
- [x] BONUS_NUMBER

#### OutputMessage Enum
- [x] TICKET_PURCHASE
- [x] STATISTICS_HEADER
- [x] STATISTICS_DIVIDER
- [x] RANK_STATISTIC
- [x] PROFIT_RATE

---

## ⚠️ 예외 처리

### 공통 예외

#### InvalidInputException
- [x] 입력 형식 오류 처리

#### InvalidPurchaseAmountException
- [x] 구입 금액 검증 실패 처리

---

### 도메인 예외

#### InvalidLottoNumberException
- [x] 6개 아님
- [x] 1~45 범위 벗어남
- [x] 중복 존재

#### InvalidBonusNumberException
- [x] 1~45 범위 벗어남
- [x] 당첨 번호와 중복

---

### 에러 메시지

#### CommonErrorMessage Enum
- [x] INVALID_INPUT
- [x] NOT_A_NUMBER
- [x] NOT_POSITIVE
- [x] INVALID_AMOUNT_UNIT
- [x] BELOW_MINIMUM

#### ClassicLottoErrorMessage Enum
- [x] INVALID_LOTTO_SIZE
- [x] INVALID_LOTTO_RANGE
- [x] DUPLICATE_LOTTO_NUMBERS
- [x] INVALID_BONUS_RANGE
- [x] DUPLICATE_BONUS_NUMBER

---

## ✅ 테스트

### 단위 테스트

#### LottoTest
- [x] 6개 아니면 예외
- [x] 중복 있으면 예외
- [x] 범위 벗어나면 예외
- [x] 정상 생성
- [x] `countMatches()` 테스트
- [x] `contains()` 테스트

#### RankTest
- [x] 6개 일치 → FIRST
- [x] 5개+보너스 → SECOND
- [x] 5개 일치 → THIRD
- [x] 4개 일치 → FOURTH
- [x] 3개 일치 → FIFTH
- [x] 2개 이하 → NONE
- [x] 상금 확인

#### LottoGeneratorTest
- [x] 지정 개수만큼 생성
- [x] 각 로또 6개 번호
- [x] 1~45 범위
- [x] 중복 없음

#### InputParserTest
- [x] 구입 금액 정상 파싱
- [x] 1,000원 단위 아님 → 예외
- [x] 숫자 아님 → 예외
- [x] 음수 → 예외
- [x] 당첨 번호 정상 파싱
- [x] 공백 포함 파싱
- [x] 당첨 번호 숫자 아님 → 예외
- [x] 보너스 번호 정상 파싱
- [x] 보너스 번호 숫자 아님 → 예외

---

### 통합 테스트

#### LottoResultIntegrationTest
- [x] 로또 생성
- [x] 당첨 번호 설정
- [x] 등수 판정
- [x] 통계 집계
- [x] 총 상금 계산
- [x] 수익률 계산

#### ApplicationTest
- [x] 전체 플로우 실행

---

## 🎯 핵심 검증

### 불변성
- [x] Lotto.numbers final
- [x] getNumbers() 방어적 복사
- [x] WinningNumbers 필드 final
- [x] Rank enum 불변

### 예외 처리
- [x] "[ERROR]" 접두사
- [x] IllegalArgumentException 계열
- [x] 명확한 예외 타입
- [x] 재입력 처리

### 코드 품질
- [x] indent depth ≤ 2
- [x] 메서드 길이 ≤ 15줄
- [x] else 금지
- [x] Enum 활용
- [x] 의미 있는 이름

### 출력 형식
- [x] 번호 오름차순 정렬
- [x] 상금 천 단위 쉼표
- [x] 수익률 소수점 첫째 자리
- [x] 통계 5등→1등 순서
- [x] 낙첨 등수도 "0개" 표시

---

## 📦 패키지 구조

```
lotto/
├── Application.java
├── engine/
│   └── GameEngine.java
├── game/
│   ├── Game.java
│   └── classicLotto/
│       ├── LottoGame.java
│       ├── Lotto.java
│       ├── InputParser.java
│       ├── domain/
│       │   ├── WinningNumbers.java
│       │   ├── Rank.java
│       │   ├── LottoResult.java
│       │   ├── LottoGenerator.java
│       │   └── exception/
│       │       ├── InvalidLottoNumberException.java
│       │       └── InvalidBonusNumberException.java
│       └── view/
│           ├── InputView.java
│           ├── OutputView.java
│           ├── dto/
│           │   ├── TicketPurchaseDTO.java
│           │   ├── WinningStatisticsDTO.java
│           │   └── RankStatistic.java
│           └── messages/
│               ├── InputMessage.java
│               ├── OutputMessage.java
│               └── ClassicLottoErrorMessage.java
└── common/
    ├── exception/
    │   ├── InvalidInputException.java
    │   └── InvalidPurchaseAmountException.java
    └── messages/
        └── CommonErrorMessage.java
```
