import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyzer {
    // Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> keywords = new ArrayList<String>(Arrays.asList("as", "as?", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in", "!in", "interface", "is", "!is", "null", "object", "package", "return", "super", "this", "throw", "true", "try", "typealias", "val", "var", "when", "while"));

    // Soft Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> softKeywords = new ArrayList<String>(Arrays.asList("by", "catch", "constructor", "delegate", "dynamic", "field", "file", "finally", "get", "import", "init", "param", "property", "receiver", "set", "setparam", "where"));

    // Modifier Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> modifierKeywords = new ArrayList<String>(Arrays.asList("actual", "abstract", "annotation", "companion", "const", "crossinline", "data", "enum", "expect", "external", "final", "infix", "inline", "inner", "internal", "lateinit", "noinline", "open", "operator", "out", "override", "private", "protected", "public", "reified", "sealed", "suspend", "tailrec", "vararg"));


}
