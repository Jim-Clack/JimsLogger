## Jim's Logger - JLogger ##

This is a Java general-purpose Logger that is useful as an exercise, example, or practical solution.
It is customizable via configuration settings or by extending classes or implementing interfaces, such as:

 `IAppender`   
 `IConfiguration`  
 `ITextFormatter`  
 `BaseLogLevelGetter`   

### Levels:  ###

 `Level.Trace`     
 `Level.Diag`     
 `Level.Info`     
 `Level.Warn`      
 `Level.Error`

### Logger Methods:  ###

 `Logger.log(level, message);`        
 `Logger.log(level, message, exception);`      
 `Logger.log(level, message, vararg, ... vararg);`      
 `TRACE(message);`      
 `TRACE(message, exception);`     
 `TRACE(message, vararg, ... vararg);`      
 `DIAG( ... );`      
 `INFO( ... );`      
 `WARN( ... );`      
 `ERROR( ... );`  

### LogManager Methods:  ###

 `getInstance();            // static (singleton)`

 `getLogger();              // for current class`    
 `getLogger(Xyz.class);     // for specified class`      
 `getLogger("x.y.Z");       // for specified class`   
 `getLogger("issue321");    // specialized logging`

 `setLevel(level, "x.y.Z"); // for specified class`    
 `setLevel(level, "x.y")    // all classes in x.y`   
 `setLevel(level, "");      // for all classes`

### Configuration Properties ###

 `jlogger.appenders.list___"x.y.ZzzAppender"_Comma-delimited list`     
 `jlogger.default.level____"Warn"____________See LogLevel.java`      
 `jlogger.console.prefix___"@t @c [@L]: "____See TextFormatter.java`     
 `jlogger.logfile.prefix___"@t @c [@L]: "____See TextFormatter.java`     
 `jlogger.logfile.name_____"jlog.log"________See FileAppender.java`     
 `jlogger.logfile.kmaxsize_"100"_____________See FileAppender.java`     
 `jlogger.logfile.backups__"10"______________See FileAppender.java`

### Replacement Symbols ###

All replacement symbols begin with @, followed by (A) or (B) below   

(A) 1..9  vararg parameter number (one-based) followed by...   

 * s     string
 * b     boolean as T or F
 * B     boolean as true or false  
 * i     integer or long  
 * h     integer or long in hexadecimal  
 * f     floating point or double  
 * d     date and time in local format  
 * D     date and time in UTC format  
 * e     exception message  
 * E     exception message and trace  
 * t     object toString()  
 * o     object/array dump, shallow  
 * O     object/array dump, deep (not yet implemented)  

(B) The following are from the LogEvent, NOT vararg arg values  

 * l     LogLevel value  
 * L     LogLevel name  
 * d     timestamp date short local form  
 * D     timestamp date long UTC form  
 * t     timestamp time short local form  
 * T     timestamp time long UTC form  
 * u     timestamp ate and time short local form  
 * U     timestamp date and time UTC form  
 * n     timestamp nanoseconds  
 * e     exception message  
 * E     exception message and trace  
 * m     calling method name  
 * c     calling class.method name  
 * p     calling package.class.method name  
 * p     calling package.class.method name (package not abbreviated)  
 * h     calling thread name  
 * @     two @-signs (@@) are escaped to a single @  

Please note that...    

 * If a value is null then typically it will be replaced with (null)  
 * If a value is cannot be processed then it will be replaced with ###  
 * If a symbol is corrupt then it will be replaced with ???   
 * Some symbols have a different meaning when used with an arg#  
 * Class names (m, M, c, and C) include an abbreviated package prefix   

### TO-DO ###

 * Create an slf4j adapter   
 * Create a Log4j2Formatter (w/log4j2 symbols)   
 * Write more Appenders (JSON, DB, etc.)   
 * Add some Unit Tests    
 * Roll up repeated events (Configurable)    