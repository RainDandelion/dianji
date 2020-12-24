public class test {
    public static void main(String[] args) {
        String s = "f5 05 02 00 00 09 ff ff ff ff ff ff 12";
        String s1 = s.replaceAll(" ", "");
        System.out.println(s1);
    }
}
