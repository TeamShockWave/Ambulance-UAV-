package gnu.expr;

import gnu.lists.AbstractFormat;
import gnu.lists.Consumer;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import java.io.Writer;
import kawa.Shell;

public class CompiledModule {
    Object cookie;
    Language language;
    ModuleExp mexp;

    public CompiledModule(ModuleExp mexp2, Object cookie2, Language language2) {
        this.mexp = mexp2;
        this.cookie = cookie2;
        this.language = language2;
    }

    public static CompiledModule make(Class clas, Language language2) {
        return new CompiledModule((ModuleExp) null, clas, language2);
    }

    public void evalModule(Environment env, CallContext ctx) throws Throwable {
        Language saveLang = Language.setSaveCurrent(this.language);
        Environment saveEnv = Environment.setSaveCurrent(env);
        try {
            ModuleExp.evalModule2(env, ctx, this.language, this.mexp, this.cookie);
        } finally {
            Language.restoreCurrent(saveLang);
            Environment.restoreCurrent(saveEnv);
        }
    }

    public void evalModule(Environment env, OutPort out) throws Throwable {
        CallContext ctx = CallContext.getInstance();
        Consumer saveConsumer = ctx.consumer;
        boolean print = ModuleBody.getMainPrintValues();
        AbstractFormat saveFormat = out.objectFormat;
        ctx.consumer = print ? Shell.getOutputConsumer(out) : new VoidConsumer();
        try {
            evalModule(env, ctx);
        } finally {
            if (ctx.consumer instanceof Writer) {
                ((Writer) ctx.consumer).flush();
            }
            ctx.consumer = saveConsumer;
            out.objectFormat = saveFormat;
        }
    }

    public Object evalToResultValue(Environment env, CallContext ctx) throws Throwable {
        int oldIndex = ctx.startFromContext();
        try {
            evalModule(env, ctx);
            return ctx.getFromContext(oldIndex);
        } catch (Throwable ex) {
            ctx.cleanupFromContext(oldIndex);
            throw ex;
        }
    }
}
