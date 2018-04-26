
# AopLib
### 1.控制方法调用的间隔 Debounce(如下,调用后，再次次5秒后调用才有效）
```
     @Debounce(value = 5000,mark = "请求网络")
        public void toast(){
            System.out.println("toast");
            Toast.makeText(this,"cccccccc",Toast.LENGTH_LONG).show();
        }
        toast();
 ```
### 2.UI标记的在主线程执行 NewThread标记的在子线程执行 配合CancelThread 在销毁时取消事件，防止内存泄漏
`不支持有返回值的方法，请用回调`
```aidl
    //默认标记都是类名.java 延迟为0,重复执行为false, period重复执行间隔
    @UI(delay = 3000,value = "主线程",repate=false,period=2000)
    private void show(CharSequence s) {
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }

    @UI
    private void showxc(String s) {
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }
    @NewThread(value = "子线程")
    @Debounce(value = 2000)
    public void toast() {
        System.out.println("MainActivity2子线程 " + java.lang.Thread.currentThread().getId());
        try {
            URL url = new URL("https://read.qidian.com/chapter/6wiYP-yFPlNrZK4x-CuJuw2/lLgJiXhXccDgn4SMoDUcDQ2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                showxc("开始解析...");
                Spanned text = Html.fromHtml(stringBuffer.toString());
                showxc("完成解析，准备延迟3s展示");
                show(text);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
     
        @CancelThread(value = {"主线程","子线程"})
        void onDestory(){}
```
