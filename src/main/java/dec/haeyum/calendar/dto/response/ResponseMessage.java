package dec.haeyum.calendar.dto.response;

public interface ResponseMessage {

    // 200
    String success = "success";

    String serverError = "서버 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.";

    String notExistedDate = "유효하지 않은 날짜 형식 입니다.(yyyy-MM-dd)";

    String duplicatedDate = "이미 가장 최신 달력 입니다.";
    String notExistedBoundary = "검색할 수 있는 범위를 초과했습니다.";
    String notExistedData = "찾을 수 있는 달력이 없습니다.";
}
