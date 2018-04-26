
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
### 2.@UI @NewThread @CancelThread 
### 基于Aop的 线程切换实现
-  转载请注明出处哦，谢谢！
#### 先说说用法
-  假如你想指定一个方法在子线程运行，如下该方法在子线程运行
```
@NewThread
void thread(){
//请求网络
}
```
-  假如我想要延迟执行呢？如下调用后将在延迟1秒后执行
```
@NewThread（delay = 1000）
void thread(){
//请求网络
}
```
-  假如我想要心跳执行呢？如下调用后将每10秒执行一次，第一次延迟1秒
```
@NewThread（delay = 1000，repate = true, period = 10000）
void thread(){
//请求网络
}
```
-  有调用就得有取消啊，不然内存溢出怎么办？
```
   @CancelThread
    private void cancel() {
    }
//调用就会取消了
 cancel()；
```
- 如果不配置默认任务名称，将以 类名.java作为任务名，如果有不同任务要求，可以指定任务名，定点取消
```
    @CancelThread("子线程")
    private void cancel() {
    }

    @NewThread(value = "子线程", repate = true, period = 10000,delay = 1000)
    public void jump() {}
    //调用取消value值为"子线程"的任务
     cancel();
```
### UI线程 和NewThread用法相同，就不重复说了。注意要和CancelThread搭配使用，以免内存溢出哦

## 实现
- Aop的原理啥的就不说了，推荐下文章
[Android中的aop编程](https://www.jianshu.com/p/0fa8073fd144)
- 主要是运用了一个handler进行主线程切换，Thread进行子线程的切换
- 也没啥技术含量，觉得有趣就拿来分享了
##### 以下是我的测试用例，基本没发现有什么问题
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = findViewById(R.id.bt);

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showxc("正在获取...");
                if (!start) {
                    start = true;
                    toast();
                }
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                start=false;
            }
        });
        main();
        viewById.setText("点击请求网络吧,延迟3秒显示结果\n @UI @NewThread都可以设置延迟时间");
        System.out.println("MainActivity2主线程 " + java.lang.Thread.currentThread().getId());
    }

    @CancelThread("子线程")
    private void cancel() {
    }

    @NewThread(value = "子线程", repate = true, period = 10000,delay = 1000)
    @Debounce(value = 2000)
    public void toast() {
        showxc("正在获取...");
        System.out.println("------------------start------------------------toast");
        System.out.println("Thread子线程 " + java.lang.Thread.currentThread().getId());
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
                viewById.getLayoutParams();
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
        System.out.println("-----------------end-------------------------toast");
    }

    @UI(delay = 3000, value = "主线程")
    private void show(CharSequence s) {
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }

    @UI
    private void showxc(String s) {
        aLong = System.currentTimeMillis();
        TextView tv = findViewById(R.id.content);
        tv.setText(s);
    }

    @UI(repate = true, delay = 2000)
    public void main() {
        System.out.println("Thread主线程2 " + java.lang.Thread.currentThread().getId());
    }

    @CancelThread(value = {"主线程", "子线程"})
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

```

##最后
-  转载请注明出处哦，谢谢！
-  对了，上面的不支持有返回值的方法哦，得改成无返回+回调后再用
- 比较懒，就不解释了，有兴趣的话，看代码去吧
[github地址 github地址 github地址](https://github.com/While1true/AopLib)
