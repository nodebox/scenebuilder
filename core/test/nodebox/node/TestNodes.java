package nodebox.node;

public class TestNodes {

    @Category("Test")
    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }

    @Category("Test")
    public static class Number extends Node {
        public IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue.get());
        }
    }

    @Category("Test")
    public static class Add extends Node {
        public IntPort pValue1 = new IntPort(this, "v1", Port.Direction.INPUT);
        public IntPort pValue2 = new IntPort(this, "v2", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue1.get() + pValue2.get());
        }
    }

    @Category("Test")
    public static class Multiply extends Node {
        public IntPort pValue1 = new IntPort(this, "v1", Port.Direction.INPUT);
        public IntPort pValue2 = new IntPort(this, "v2", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue1.get() * pValue2.get());
        }
    }

    @Category("Test")
    public static class ConvertToUppercase extends Node {
        public StringPort pString = new StringPort(this, "string", Port.Direction.INPUT);
        public StringPort pOutput = new StringPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pString.get().toUpperCase());
        }
    }
}
