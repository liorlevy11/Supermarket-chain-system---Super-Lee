package ServiceLayer;

public class ResponseT<T> extends Response
{
        private final T value;
        public ResponseT(T value, String msg)
        {
                super(msg);
                this.value = value;
        }
        @Override
        public String Content(){
                if(this.isErrorOccurred()){
                        return this.getErrorMessage();
                }else{
                        return value.toString();
                }
        }

        public static  <T> ResponseT<T> fromValue(T value)
        {
                return new ResponseT(value, null);
        }

        public static  <T> ResponseT<T> fromError(String msg)
        {
                if(msg == null)
                        return new ResponseT(new Object(), "null msg");
                return new ResponseT(new Object(), msg);
        }

        public T getValue() {
                return value;
        }
}

