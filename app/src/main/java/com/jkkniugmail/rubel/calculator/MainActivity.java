package com.jkkniugmail.rubel.calculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jkkniugmail.rubel.calculator.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    protected TextView display;
    protected byte validDot;
    protected byte validAns;
    protected byte validOperator;
    protected byte validSyntax;
    protected byte flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        display = (TextView) findViewById(R.id.display);
        setDefault();
    }


    protected void writeInput(String str){
        if(flag==0){
            if(str=="."){
                display.setText("0"+str);
                flag = 1;
            }
            else if(str=="0"){
                flag=0;
            }
            else if(validAns == 1 && (str == "+"||str == "-"||str == "*"||str == "/")){
                String result = display.getText().toString();
                if (checkInfityResult(result)){
                    str = "0" + str;
                }
                else
                    str = result + str;
                display.setText(str);
                flag = 1;
            }
            else if(validAns == 0 &&(str == "+"||str == "-"||str == "*"||str == "/")){
                display.setText("0" + str);
                flag = 1;
            }
            else {
                display.setText(str);
                flag = 1;
            }
        }
        else{
            CharSequence ch = display.getText();
            int len = ch.length();
            if(str=="0"&&ch.charAt(len-1)=='0'&&(ch.charAt(len-2)=='+'||ch.charAt(len-2)=='-'||ch.charAt(len-2)=='*'||ch.charAt(len-2)=='/')){
                str = ch.toString();
            }
            else if(str=="."&&(ch.charAt(len-1)=='+'||ch.charAt(len-1)=='-'||ch.charAt(len-1)=='*'||ch.charAt(len-1)=='/')){
                str = ch.toString() + "0" + str;
            }
            else{
                str = ch.toString() + str;
            }

            display.setText(str);
        }

    }


    public void draw9(View view) {
        writeInput("9");
        validOperator = 1;
        validAns = 0;
        validSyntax = 1;
    }
    public void draw8(View view) {
        writeInput("8");
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
    }
    public void draw7(View view) {
        writeInput("7");
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
    }
    public void draw6(View view) {
        writeInput("6");
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
    }
    public void draw5(View view) {
        writeInput("5");
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
    }
    public void draw4(View view) {
        writeInput("4");
        validOperator = 1;
        validAns = 0;
        validSyntax = 1;
    }
    public void draw3(View view) {
        writeInput("3");
        validOperator = 1;
        validAns = 0;
        validSyntax = 1;
    }
    public void draw2(View view) {
        writeInput("2");
        validOperator = 1;
        validAns = 0;
        validSyntax = 1;
    }
    public void draw1(View view) {
        writeInput("1");
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
    }

    public void draw0(View view) {
        writeInput("0");
        validOperator = 1;
        validAns = 0;
        validSyntax = 1;
    }
    public void drawDot(View view) {
        if(validDot==1){
            writeInput(".");
            validDot = 0;
            validOperator = 1;
            validAns = 0;
            validSyntax = 1;
        }
    }
    public void drawPlus(View view){
        if(validOperator==1){
            writeInput("+");
            validDot = 1;
            validAns = 0;
            validOperator = 0;
            validSyntax = 0;
        }
    }
    public void drawMinus(View view){
        if(validOperator==1){
            writeInput("-");
            validDot = 1;
            validOperator = 0;
            validAns = 0;
            validSyntax = 0;
        }
    }
    public void drawDivisor(View view){
        if(validOperator==1){
            writeInput("/");
            validDot = 1;
            validOperator = 0;
            validAns = 0;
            validSyntax = 0;
        }
    }
    public void drawMultiply(View view){
        if(validOperator==1){
            writeInput("*");
            validDot = 1;
            validAns = 0;
            validOperator = 0;
            validSyntax = 0;
        }
    }
    public void findResult(View view){

        if(validSyntax==1){
            String str;
            long x;
            str=display.getText().toString();
            double result = eval(str);
            x = (long) result;
            if(result-x==0){
                str = Long.toString(x);

            }
            else {
                str = Double.toString(result);
            }
            display.setText(str);
            setDefault();
            validAns = 1;
        }

    }

    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }


            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


    public void clearDisplay(View view) {
        display.setText("0");
        setDefault();
    }

    public void deleteTop(View view) {
        String str = display.getText().toString();
        int len = str.length();
        char c = str.charAt(len-1);


        if(c =='/'||c =='*'||c =='-'||c =='+'){
            validOperator = 1;
            validSyntax = 1;
            if(len!=1){
                int index = len-2;
                char ch[] = new char[len-1];

                ch = str.toCharArray();
                do{
                    if(ch[index]=='.'){
                        validDot = 0;
                        break;
                    }
                    if(ch[index]=='+'||ch[index]=='-'||ch[index]=='*'||ch[index]=='/'){
                        break;
                    }
                    index = index - 1;
                }while (index>=0);
            }


        }
        else if(c=='.'){
            validDot = 1;
        }

        if (len == 1||checkInfityResult(str)){
            str = "0";
            setDefault();
        }
        else {
            str = str.substring(0, str.length() - 1);
        }

        display.setText(str);
    }

    boolean checkInfityResult(String str){
        String pattern = "[InfinityNaN]";
        Pattern p = Pattern.compile(pattern);
        Matcher m =  p.matcher(str);
        if(m.find()){
            return true;
        }
        else
            return false;
    }
    void setDefault(){
        validDot = 1;
        validOperator = 1;
        validSyntax = 1;
        validAns = 0;
        flag = 0;

    }
}
