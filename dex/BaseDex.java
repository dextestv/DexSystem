package dex;

abstract public class BaseDex {
    CharSequence dex_name;
    CharSequence dex_package;

    public abstract void start();
    void create_settings(){}

    public abstract void self_destruct();

}
