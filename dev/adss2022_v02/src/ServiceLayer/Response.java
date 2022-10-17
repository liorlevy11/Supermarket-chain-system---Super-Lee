package ServiceLayer;

public class Response {
    ///<summary>Class <c>Response</c> represents the result of a call to a void function.
    ///If an exception was thrown, <c>ErrorOccurred = true</c> and <c>ErrorMessage != null</c>.
    ///Otherwise, <c>ErrorOccurred = false</c> and <c>ErrorMessage = null</c>.</summary>
    private String errorMessage;
    public Response(){}
    public Response(String msg)
        {
            this.errorMessage = msg;
        }

    public String getErrorMessage() {
        return errorMessage;
    }
    public String Content(){
        return this.errorMessage;
    }

    public boolean isErrorOccurred() {
        return (errorMessage != null && !errorMessage.equals(""));
    }

}
