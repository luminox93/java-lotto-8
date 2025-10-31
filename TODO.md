# 🔨 구현 상세 체크리스트

> 💡 설계 원칙과 이유는 [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)를 참고하세요.

---

## 📐 설계 원칙 확인

### YAGNI (You Aren't Gonna Need It)
- [ ] ❌ Lottery 인터페이스 만들지 않기 (현재 요구사항에 로또만 있음)
- [ ] ❌ 불필요한 추상화 지양

### Tell, Don't Ask
- [ ] ✅ getter 최소화, 비즈니스 메서드 제공
- [ ] ❌ `sortedNumbers()` 같은 View 전용 getter 제거

### DTO 사용
- [ ] ✅ Domain 객체를 View에 직접 노출하지 않기
- [ ] ✅ `toTicketDto()`, `toStatisticsDto()` 변환 메서드 제공

---

## 🏗️ 도메인 클래스 구조

### Lotto 클래스 (불변 객체)
- [ ] `private final List<Integer> numbers` 필드
- [ ] 생성자: `List.copyOf(numbers)` 방어적 복사
  - [ ] 외부 리스트 변경이 내부에 영향 안 주도록
- [ ] 생성자: `LottoValidator.validateNumbers(numbers, policy)` 호출
- [ ] `numbers()` 메서드: 방어적 복사 반환 (필요시만 - Tell, Don't Ask)
  - [ ] `List.copyOf(numbers)` 반환
- [ ] 검증 로직 (LottoValidator에 위임)
  - [ ] 6개 검증
  - [ ] 1~45 범위 검증
  - [ ] 중복 검증
- [ ] ✅ `countMatches(List<Integer> winningNumbers)` 비즈니스 메서드
  - [ ] 교집합 개수 계산
- [ ] ✅ `contains(int number)` 비즈니스 메서드
  - [ ] 보너스 번호 확인용
- [ ] ✅ `toTicketDto()` DTO 변환 메서드 (필수!)
  - [ ] 정렬된 번호로 LottoTicketDto 생성
  - [ ] View가 Lotto 도메인 직접 접근 차단
- [ ] ❌ `sortedNumbers()` 같은 View 전용 getter 제거
  - [ ] View를 위한 메서드는 DTO로 대체

### LottoGenerator 클래스
- [ ] 로또 생성 책임 분리 (Lotto는 도메인 객체, 생성은 별도 책임)
- [ ] 필드: `private final LottoPolicy policy`
- [ ] `List<Lotto> generate(int count)` 메서드
  - [ ] count만큼 반복
  - [ ] 각각 `createLotto()` 호출
- [ ] `private Lotto createLotto()` 메서드
  - [ ] `Randoms.pickUniqueNumbersInRange(policy.minNumber(), policy.maxNumber(), policy.numberCount())` 사용
  - [ ] 반환값으로 새 Lotto 생성
- [ ] ❌ 인터페이스 만들지 않기 (YAGNI - 테스트는 고정 번호로 Lotto 직접 생성)

### WinningNumbers 클래스
- [ ] 당첨 번호 6개 + 보너스 번호 1개 관리
- [ ] `private final List<Integer> numbers` - 당첨 번호 6개
- [ ] `private final int bonusNumber` - 보너스 번호
- [ ] 생성자 검증
  - [ ] 당첨 번호 6개 검증 (Lotto와 동일)
  - [ ] 보너스 번호가 1~45 범위인지
  - [ ] 보너스 번호가 당첨 번호와 중복되지 않는지
- [ ] `Rank match(Lotto lotto)` 메서드
  - [ ] 일치 개수 계산
  - [ ] 보너스 일치 여부 확인
  - [ ] Rank 반환

### Rank Enum
- [ ] 등수 상수 정의
  - [ ] `FIRST(6, false, 2_000_000_000L)`
  - [ ] `SECOND(5, true, 30_000_000L)`
  - [ ] `THIRD(5, false, 1_500_000L)`
  - [ ] `FOURTH(4, false, 50_000L)`
  - [ ] `FIFTH(3, false, 5_000L)`
  - [ ] `MISS(0, false, 0L)` - 낙첨
- [ ] 필드 (모두 private final)
  - [ ] `int matchCount` - 일치 개수
  - [ ] `boolean bonusMatch` - 보너스 일치 여부
  - [ ] `long prize` - 상금
- [ ] `static Rank from(int matchCount, boolean bonusMatch)` 정적 팩토리 메서드
  - [ ] matchCount와 bonusMatch로 등수 판정
  - [ ] 6개 → FIRST
  - [ ] 5개 + bonus → SECOND
  - [ ] 5개 → THIRD
  - [ ] 4개 → FOURTH
  - [ ] 3개 → FIFTH
  - [ ] 그 외 → MISS
- [ ] `long prize()` - 상금 반환 (getter이지만 Domain 내부에서만 사용)

### LottoResult 클래스
- [ ] 당첨 결과 집계
- [ ] `private final Map<Rank, Integer> result` - 등수별 개수 (EnumMap 권장)
- [ ] 생성자: Map 초기화 (모든 Rank를 0개로)
- [ ] `void addRank(Rank rank)` - 등수 추가
- [ ] `int countOf(Rank rank)` - 특정 등수 개수 조회
- [ ] `long calculateTotalPrize()` - 총 상금 계산
  - [ ] 각 등수별 (개수 × 상금) 합산
- [ ] `double calculateProfitRate(int purchaseAmount)` - 수익률 계산
  - [ ] (총 상금 / 구입 금액) × 100
  - [ ] 반올림은 출력 단계에서 처리
- [ ] ✅ `StatisticsDto toStatisticsDto()` DTO 변환 메서드 (필수!)
  - [ ] 각 등수별 개수와 상금을 DTO로 변환
  - [ ] View가 Rank enum과 LottoResult 내부를 모르도록

### LottoValidator 클래스
- [ ] 검증 로직 모음 (static 메서드)
- [ ] `validateNumbers(List<Integer> numbers, LottoPolicy policy)`
  - [ ] 6개 검증: `numbers.size() != policy.numberCount()`
  - [ ] 1~45 범위 검증: `number < policy.minNumber() || number > policy.maxNumber()`
  - [ ] 중복 검증: `numbers.size() != new HashSet<>(numbers).size()`
  - [ ] 실패 시 `IllegalArgumentException` 발생

### LottoPolicy 클래스
- [ ] 정책 상수 정의 (public static final)
- [ ] `MIN_NUMBER = 1`
- [ ] `MAX_NUMBER = 45`
- [ ] `NUMBER_COUNT = 6`
- [ ] `PRICE = 1000`
- [ ] getter 메서드: `minNumber()`, `maxNumber()`, `numberCount()`, `price()`

---

## 📦 DTO 레이어

### LottoTicketDto
- [ ] ✅ 로또 번호 출력용 데이터 전달 객체 (필수!)
- [ ] `record LottoTicketDto(List<Integer> numbers)`
- [ ] 정렬된 번호를 담음
- [ ] View가 Lotto 도메인을 직접 접근하지 않도록 차단

### StatisticsDto
- [ ] ✅ 당첨 통계 출력용 데이터 전달 객체 (필수!)
- [ ] `record StatisticsDto(...)` 형태
- [ ] 필드 (Flat 구조 - 간단함 우선)
  - [ ] `int fifthCount, long fifthPrize`
  - [ ] `int fourthCount, long fourthPrize`
  - [ ] `int thirdCount, long thirdPrize`
  - [ ] `int secondCount, long secondPrize`
  - [ ] `int firstCount, long firstPrize`
- [ ] record는 자동으로 getter 생성 (데이터 전달 목적이므로 OK)
- [ ] View가 Domain(Rank, LottoResult)을 모르도록 함

---

## ⚙️ Service 레이어

### 입력 Reader 클래스들
- [ ] `LottoPurchaseAmountReader`: 구입 금액 입력 및 검증
- [ ] `LottoWinningInputReader`: 당첨 번호, 보너스 번호 입력 및 검증
- [ ] 검증 실패 시 `IllegalArgumentException` 발생

### 출력 Presenter 클래스들
- [ ] `LottoTicketPresenter`: 로또 출력 (Domain → DTO 변환)
- [ ] `LottoResultPresenter`: 결과 출력 (Domain → DTO 변환)

### 비즈니스 로직 클래스들
- [ ] `LottoWinningChecker`: 당첨 확인
- [ ] `LottoProfitCalculator`: 수익률 계산

---

## 🎮 Engine 레이어

### RetryExecutor
- [ ] 재입력 처리 (while + try-catch 패턴)
- [ ] `execute(Supplier<T>)` 메서드
- [ ] `IllegalArgumentException`만 처리

### GameEngine
- [ ] 전체 게임 플로우 제어
- [ ] 필요한 Service/View 의존성 주입
- [ ] `play()` 메서드
  - [ ] 구입 금액 입력 (RetryExecutor 사용)
  - [ ] 로또 발행
  - [ ] ✅ Lotto → LottoTicketDto 변환
  - [ ] 로또 출력
  - [ ] 당첨 번호 입력 (RetryExecutor 사용)
  - [ ] 보너스 번호 입력 (RetryExecutor 사용)
  - [ ] WinningNumbers 생성
  - [ ] 당첨 확인
  - [ ] ✅ LottoResult → StatisticsDto 변환
  - [ ] 결과 출력

### GameModule
- [ ] 의존성 조립 (DI 컨테이너)
- [ ] GameEngine 생성 및 반환

---

## 🖥️ View 레이어

### InputView 클래스
- [ ] `int readPurchaseAmount()` - 구입 금액 입력
  - [ ] "구입금액을 입력해 주세요." 출력
  - [ ] Console.readLine() 사용
  - [ ] 파싱 및 검증
  - [ ] 예외 발생 시 IllegalArgumentException throw
- [ ] `List<Integer> readWinningNumbers()` - 당첨 번호 입력
  - [ ] 빈 줄 출력
  - [ ] "당첨 번호를 입력해 주세요." 출력
  - [ ] Console.readLine() 사용
  - [ ] 쉼표로 split
  - [ ] trim 후 Integer.parseInt
  - [ ] List<Integer> 반환
  - [ ] 예외 발생 시 IllegalArgumentException throw
- [ ] `int readBonusNumber()` - 보너스 번호 입력
  - [ ] 빈 줄 출력
  - [ ] "보너스 번호를 입력해 주세요." 출력
  - [ ] Console.readLine() 사용
  - [ ] 파싱 및 검증
  - [ ] 예외 발생 시 IllegalArgumentException throw
- [ ] 입력 파싱 유틸리티 메서드
  - [ ] `private int parseInteger(String input)` - 예외 처리 포함
  - [ ] `private List<Integer> parseNumbers(String input)` - 쉼표 분리

### OutputView 클래스
- [ ] ✅ `void printTickets(List<LottoTicketDto> dtos)` - 구매 로또 출력 (DTO 사용!)
  - [ ] ❌ `List<Lotto>` 직접 받지 않기
  - [ ] 빈 줄 출력
  - [ ] "N개를 구매했습니다." 출력
  - [ ] 각 dto.numbers() 출력
  - [ ] 형식: `[8, 21, 23, 41, 42, 43]`
- [ ] ✅ `void printStatistics(StatisticsDto dto)` - 당첨 통계 출력 (DTO 사용!)
  - [ ] ❌ Domain(Rank, LottoResult) 직접 받지 않기
  - [ ] 빈 줄 출력
  - [ ] "당첨 통계" 출력
  - [ ] "---" 출력
  - [ ] 5등부터 1등까지 순서대로 출력
  - [ ] 형식: "3개 일치 (5,000원) - 1개"
  - [ ] DTO에서 개수와 상금을 직접 가져옴
  - [ ] 금액 포맷팅: `String.format("%,d", dto.fifthPrize())`
  - [ ] View는 Rank enum을 모름
- [ ] `void printProfitRate(double profitRate)` - 수익률 출력
  - [ ] 형식: "총 수익률은 62.5%입니다."
  - [ ] `String.format("%.1f", profitRate)` 사용
- [ ] `void printError(String message)` - 에러 메시지 출력

---

## 📥 입력 기능

### 1. 구입 금액 입력
- [ ] 사용자로부터 구입 금액 문자열 입력받기
- [ ] 입력값 검증
  - [ ] null 또는 빈 문자열 검증
  - [ ] 공백만 있는 경우 검증 (trim 후 빈 문자열)
  - [ ] 숫자 형식 검증 (Integer.parseInt 가능 여부)
  - [ ] 쉼표 포함 입력("1,000", "10,000") 거부
  - [ ] 한글 포함 입력("1000원", "천원") 거부
  - [ ] 양의 정수 검증 (0, 음수 거부)
  - [ ] Integer 범위 초과 검증
  - [ ] 소수점 입력("1000.5") 거부
- [ ] 최소 구매 단위 검증 (MINIMUM_PURCHASE_UNIT = 1000)
  - [ ] 1,000원 미만 입력 시 예외 발생
  - [ ] 1,000원으로 나누어떨어지지 않으면 예외 발생
- [ ] 잘못된 입력 시 에러 메시지 출력 후 재입력 요청

#### 예외 케이스
- [ ] `""` - 빈 문자열
- [ ] `"   "` - 공백만
- [ ] `null` - null 입력
- [ ] `"abc"` - 문자열
- [ ] `"1,000"` - 쉼표 포함
- [ ] `"1000원"` - 한글 포함
- [ ] `"-1000"` - 음수
- [ ] `"0"` - 0원
- [ ] `"500"` - 최소 단위 미만
- [ ] `"1500"` - 단위로 나누어떨어지지 않음
- [ ] `"1000.5"` - 소수점
- [ ] `"2147483648"` - Integer 범위 초과

---

### 2. 당첨 번호 입력
- [ ] 사용자로부터 당첨 번호 문자열 입력받기 (쉼표 구분)
- [ ] 쉼표로 문자열 분리
- [ ] 입력값 검증
  - [ ] null 또는 빈 문자열 검증
  - [ ] 정확히 6개인지 검증 (5개, 7개 거부)
  - [ ] 각 번호가 숫자 형식인지 검증
  - [ ] 각 번호가 1~45 범위인지 검증
  - [ ] 0, 음수, 46 이상 거부
  - [ ] 중복 번호 검증
  - [ ] 쉼표 연속("1,,2,3,4,5,6") 거부
  - [ ] 쉼표 뒤 공백 처리 ("1, 2, 3" → trim 필요)
  - [ ] 쉼표 없이 붙여쓴 경우("123456") 거부
- [ ] 잘못된 입력 시 에러 메시지 출력 후 재입력 요청

#### 예외 케이스
- [ ] `""` - 빈 문자열
- [ ] `null` - null 입력
- [ ] `"1,2,3,4,5"` - 5개만 입력
- [ ] `"1,2,3,4,5,6,7"` - 7개 입력
- [ ] `"1, 2, 3, 4, 5, 6"` - 쉼표 뒤 공백
- [ ] `"1 ,2, 3,4 ,5,6"` - 쉼표 앞 공백
- [ ] `"1,2,3,4,5,6,"` - 마지막 쉼표
- [ ] `"1,,2,3,4,5,6"` - 쉼표 연속
- [ ] `"1,2,,3,,4,5,6"` - 중간에 쉼표 연속
- [ ] `",,,,,"` - 쉼표만
- [ ] `"123456"` - 쉼표 없이
- [ ] `"0,1,2,3,4,5"` - 0 포함 (범위 밖)
- [ ] `"1,2,3,4,5,46"` - 46 포함 (범위 밖)
- [ ] `"1,2,3,4,5,-1"` - 음수 포함
- [ ] `"1.0,2,3,4,5,6"` - 소수점
- [ ] `"a,b,c,d,e,f"` - 전부 문자
- [ ] `"1,a,3,4,5,6"` - 일부만 문자
- [ ] `"1,2,2,3,4,5"` - 중복 (2가 두 번)
- [ ] `"1,1,1,1,1,1"` - 전부 중복

---

### 3. 보너스 번호 입력
- [ ] 사용자로부터 보너스 번호 문자열 입력받기
- [ ] 입력값 검증
  - [ ] null 또는 빈 문자열 검증
  - [ ] 공백만 있는 경우 검증
  - [ ] 숫자 형식 검증
  - [ ] 1~45 범위 검증
  - [ ] 0, 음수, 46 이상 거부
  - [ ] 소수점 거부
  - [ ] 당첨 번호 6개와 중복 검증
- [ ] 잘못된 입력 시 에러 메시지 출력 후 재입력 요청

#### 예외 케이스
- [ ] `""` - 빈 문자열
- [ ] `null` - null 입력
- [ ] `"   "` - 공백만
- [ ] `"abc"` - 문자열
- [ ] `"0"` - 0 (범위 밖)
- [ ] `"-5"` - 음수
- [ ] `"46"` - 46 (범위 밖)
- [ ] `"7.5"` - 소수점
- [ ] 당첨 번호가 `1,2,3,4,5,6`이고 보너스가 `"3"` - 중복

---

## 🎫 로또 발행 기능

### 4. 로또 번호 생성
- [ ] 구입 금액을 MINIMUM_PURCHASE_UNIT으로 나누어 구매 개수 계산
- [ ] 구매 개수만큼 로또 생성
- [ ] 각 로또는 Randoms.pickUniqueNumbersInRange(1, 45, 6) 사용
- [ ] 생성된 번호로 Lotto 객체 생성

### 5. Lotto 클래스 검증 추가
- [ ] 로또 번호가 정확히 6개인지 검증 (이미 제공됨)
- [ ] 각 번호가 1~45 범위인지 검증 (추가 필요)
- [ ] 중복 번호가 없는지 검증 (추가 필요)

---

## 🏆 당첨 확인 기능

### 6. 당첨 내역 계산
- [ ] 각 로또마다 당첨 번호와 일치하는 개수 확인
  - [ ] 교집합 크기 계산
- [ ] 5개 일치 시 보너스 번호 포함 여부 확인
- [ ] 일치 개수와 보너스 일치 여부로 등수 판정
  - [ ] 6개 일치 → 1등 (2,000,000,000원)
  - [ ] 5개 일치 + 보너스 일치 → 2등 (30,000,000원)
  - [ ] 5개 일치 → 3등 (1,500,000원)
  - [ ] 4개 일치 → 4등 (50,000원)
  - [ ] 3개 일치 → 5등 (5,000원)
  - [ ] 2개 이하 → 꽝
- [ ] 등수별 당첨 개수 집계
  - [ ] 1등 개수, 2등 개수, 3등 개수, 4등 개수, 5등 개수

### 7. 수익률 계산
- [ ] 총 상금 계산
  - [ ] (1등 개수 × 2,000,000,000) + (2등 개수 × 30,000,000) + ...
- [ ] 수익률 계산
  - [ ] (총 상금 / 구입 금액) × 100
- [ ] 소수점 둘째 자리에서 반올림
  - [ ] String.format("%.1f", 수익률) 사용

#### 엣지 케이스
- [ ] 전부 꽝인 경우 → 0.0%
- [ ] 정확히 100% → 100.0%
- [ ] 1등 당첨 → 매우 큰 수익률 (예: 25,000,000.0%)
- [ ] 0.04% → 0.0% (반올림)
- [ ] 0.05% → 0.1% (반올림)

---

## 📤 출력 기능

### 8. 구매 로또 출력
- [ ] 빈 줄 출력
- [ ] "N개를 구매했습니다." 출력
- [ ] 각 로또마다:
  - [ ] 번호를 오름차순 정렬
  - [ ] "[8, 21, 23, 41, 42, 43]" 형식으로 출력

### 9. 당첨 통계 출력
- [ ] 빈 줄 출력
- [ ] "당첨 통계" 출력
- [ ] "---" 출력
- [ ] 5등부터 1등까지 순서대로 출력
  - [ ] "3개 일치 (5,000원) - 1개" 형식
  - [ ] 당첨되지 않은 등수도 "0개"로 표시
  - [ ] 금액은 천 단위 쉼표 포함 (5,000 / 50,000 / 1,500,000 / 30,000,000 / 2,000,000,000)

### 10. 수익률 출력
- [ ] "총 수익률은 62.5%입니다." 형식
- [ ] xxx.x% 형태 (소수점 첫째 자리까지)

---

## ⚠️ 예외 처리

### 11. 에러 메시지 형식
- [ ] 모든 에러 메시지는 "[ERROR]"로 시작
- [ ] 에러 발생 사유를 명시적으로 설명
  - [ ] "[ERROR] 구입 금액은 1,000원 단위여야 합니다."
  - [ ] "[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다."
  - [ ] "[ERROR] 로또 번호는 중복될 수 없습니다."
  - [ ] "[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다."

### 12. 재입력 처리
- [ ] 잘못된 입력 발생 시 해당 입력만 다시 요청
  - [ ] 구입 금액 오류 → 구입 금액만 재입력
  - [ ] 당첨 번호 오류 → 당첨 번호만 재입력
  - [ ] 보너스 번호 오류 → 보너스 번호만 재입력
- [ ] 프로그램 종료하지 않고 재입력 반복

---

## 📚 추가 고려사항

### 상수 정의
- [ ] MINIMUM_PURCHASE_UNIT = 1000
- [ ] LOTTO_NUMBER_MIN = 1
- [ ] LOTTO_NUMBER_MAX = 45
- [ ] LOTTO_NUMBER_COUNT = 6
- [ ] 등수별 상금 (enum 또는 상수)

### 테스트 작성
- [ ] 입력 검증 테스트
- [ ] 로또 번호 생성 테스트
- [ ] 당첨 내역 계산 테스트
- [ ] 수익률 계산 테스트
- [ ] 예외 발생 테스트

---

## ⚠️ 중요 주의사항

### 설계 원칙 준수
- [ ] ❌ Lottery 인터페이스 만들지 않기 (YAGNI)
- [ ] ❌ `sortedNumbers()` 같은 View 전용 getter 제거
- [ ] ✅ `toTicketDto()`, `toStatisticsDto()` 필수 구현
- [ ] ✅ View는 항상 DTO만 받기 (Domain 직접 의존 금지)

### 프로그래밍 요구사항
- [ ] indent depth 2 이하
- [ ] 메서드 15라인 이하
- [ ] else 예약어 사용 안 함
- [ ] Enum 적용
- [ ] 단위 테스트 작성 (UI 제외)

### 커밋 규칙
- [ ] [docs/CLAUDE.md](docs/CLAUDE.md) 참고
- [ ] Claude 서명 사용 금지
- [ ] AngularJS 커밋 컨벤션 준수

---

## ✅ 완료 기준

- [ ] 모든 기능 요구사항 구현
- [ ] 모든 프로그래밍 요구사항 준수
- [ ] 모든 단위 테스트 통과
- [ ] 설계 원칙 준수 확인
  - [ ] Lottery 인터페이스 없음
  - [ ] sortedNumbers() 메서드 없음
  - [ ] DTO 변환 메서드 구현
  - [ ] View는 DTO만 의존
- [ ] 실행 결과가 요구사항과 일치
