
# AopLib
### 1.控制方法调用的间隔 Debounce(如下,调用后，再次次5秒后调用才有效）
```
     @Debounce(value = 5000,mark = "请求网络")
        public void toast(){
            System.out.println("toast");
            Toast.makeText(this,"cccccccc",Toast.LENGTH_LONG).show();
        }
        toast()；
 ```
### 2.UI标记的在主线程执行 NewThread标记的在子线程执行 配合CancelThread 在销毁时取消事件，防止内存泄漏
```aidl
//默认标记都是类名.java 延迟为0
       @UI(delay = 3000,value = "主线程")
       private void show(String s) {
            TextView tv = findViewById(R.id.content);
            System.out.println("开始解析展示" + (System.currentTimeMillis() - aLong));
            Spanned text = Html.fromHtml(s);
            System.out.println("结束解析展示" + (System.currentTimeMillis() - aLong));
            tv.setText(text);
        }
        @NewThread(delay = 3000,value = "子线程")
        private void show(String s) {
          //请求网络
        }
        @CancelThread(value = {"主线程","子线程"})
        void onDestory(){}
```
