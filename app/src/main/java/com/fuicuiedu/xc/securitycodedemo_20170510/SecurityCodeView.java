package com.fuicuiedu.xc.securitycodedemo_20170510;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class SecurityCodeView extends RelativeLayout {
    private EditText editText;//输入框，透明不可见
    private TextView[] textViews;//验证码显示框
    private StringBuffer stringBuffer = new StringBuffer();//拼接String
    private int count = 5;//用于确定string当前的位置
    private String inputContent;//得到的String


    public SecurityCodeView(Context context) {
        this(context, null);
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_security_code, this);

        editText = (EditText) findViewById(R.id.item_edittext);

        textViews = new TextView[5];//四个展示框（根据需求更改）
        textViews[0] = (TextView) findViewById(R.id.item_code_iv1);
        textViews[1] = (TextView) findViewById(R.id.item_code_iv2);
        textViews[2] = (TextView) findViewById(R.id.item_code_iv3);
        textViews[3] = (TextView) findViewById(R.id.item_code_iv4);
        textViews[4] = (TextView) findViewById(R.id.item_code_iv5);

        editText.setCursorVisible(false);//将光标隐藏
        setListener();//给editText添加监听
    }

    private void setListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重点！！！！！！
                //如果字符不为“”时才进行操作
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > 4) {
                        //当文本长度大于3位时edittext置空
                        editText.setText("");
                        return;
                    } else {
                        //将文字添加到StringBuffer中
                        stringBuffer.append(editable);
                        editText.setText("");//添加后将EditText置空，造成没有输入文字的错觉
                        count = stringBuffer.length();//记录stringBuffer长度
                        inputContent = stringBuffer.toString();
                        //文字长度为5位，则调用完成输入监听
                        if (stringBuffer.length() == 5) {
                            if (inputCompleteListener != null) {
                                inputCompleteListener.inputComplete();
                            }
                        }
                    }

                    for (int i = 0; i < stringBuffer.length(); i++) {
                        //设置textView显示文字
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                        //改变textView背景
                        textViews[i].setBackgroundResource(R.drawable.bg_verify_press);
                    }
                }
            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onKeyDelete()) return true;
                    return true;
                }
                return false;
            }
        });
    }

    //当点击删除时触发
    public boolean onKeyDelete() {
        if (count == 0) {
            count = 5;
            return true;
        }
        if (stringBuffer.length() > 0) {
            //删除相应位置的字符
            stringBuffer.delete((count - 1), count);
            count--;//string长度减一
            inputContent = stringBuffer.toString();//重新赋值
            //删除后该位置置空
            textViews[stringBuffer.length()].setText("");
            //删除后该位置背景改变
            textViews[stringBuffer.length()].setBackgroundResource(R.drawable.bg_verify);
            if (inputCompleteListener != null)
                inputCompleteListener.deleteContent(true);//有删除就通知manger

        }
        return false;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }

    //清空输入内容
    public void clearEditText() {
        stringBuffer.delete(0, stringBuffer.length());
        inputContent = stringBuffer.toString();
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText("");
            textViews[i].setBackgroundResource(R.drawable.bg_verify);
        }
    }

    //获取输入文本
    public String getEditContent() {
        return inputContent;
    }

    //输入完成监听-----接口回调
    public interface InputCompleteListener {

        //输入完成
        void inputComplete();

        //删除
        void deleteContent(boolean isDelete);
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }


}
