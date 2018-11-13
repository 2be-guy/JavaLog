import java.util.logging.*;
import java.util.*;
import java.lang.reflect.*;

public class LogLevelExample {
    private static Logger log = Logger.getLogger(LogLevelExample.class.getName());

    public static void main(String[] args) throws Exception {
        log.setUseParentHandlers(false);
        FileHandler fileHandler = new FileHandler("log.txt");
        fileHandler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT]"  + "[%2$-7s]" + "%3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        log.addHandler(fileHandler);

        setLevel(Level.ALL);
        Set<Level> levels = getAllLevels();
        int i = 1;
        for (Level level : levels) {
            log.log(level, level.getName() + " - " + (i++));
        }
    }

    public static void setLevel(Level targetLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
        System.out.println("level set: " + targetLevel.getName());
    }

    public static Set<Level> getAllLevels() throws IllegalAccessException {
        Class<Level> levelClass = Level.class;

        Set<Level> allLevels = new TreeSet<>(
                Comparator.comparingInt(Level::intValue));

        for (Field field : levelClass.getDeclaredFields()) {
            if (field.getType() == Level.class) {
                allLevels.add((Level) field.get(null));
            }
        }
        return allLevels;
    }
}