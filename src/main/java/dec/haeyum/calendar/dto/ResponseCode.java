package dec.haeyum.calendar.dto;

public interface ResponseCode {

    String success = "SU";
    // 서버 에러
    String serverError = "ISE";
    // 날짜 형식 에러
    String notExistedDate = "IVD";
    String duplicatedDate = "DD";
    // 검색 범위 초과
    String notExistedBoundary = "EB";
    String notExistedData = "ED";
    // API 호출 실패
    String apiCallFail = "AF";
}
