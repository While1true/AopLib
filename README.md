
# AopLib
1.控制方法调用的间隔 Debounce(如下,调用后，再次次5秒后调用才有效）
```
 @Debounce(value = 5000,mark = "请求网络")
    public void toast(){
        System.out.println("toast");
        Toast.makeText(this,"cccccccc",Toast.LENGTH_LONG).show();
    }
    toast()；
 ```
