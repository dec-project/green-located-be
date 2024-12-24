package dec.haeyum.searchCalender.dto;

public interface ResponseCode {

    String success = "SU";
    // 서버 에러
    String serverError = "ISE";
    // 날짜 형식 에러
    String notExistedDate = "IVD";
    String duplicatedDate = "DD";
    String notExistedBoundary = "EB";
    String notExistedData = "ED";
}
