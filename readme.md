## Jim's Logger - JLogger ##

This is a Java general-purpose Logger that is useful as an exercise, example, or practical solution.
It is customizable via configuration settings or by extending classes or implementing interfaces, such as:

 `IAppender`   
 `IConfiguration`   
 `ITextFormatter`   
 `BaseTextFormatter`    
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

 `jlogger.appenders.list____"x.y.ZzzAppender"__Comma-delimited list`     
 `jlogger.default.level_____"Warn"_____________See LogLevel.java`      
 `jlogger.console.prefix____"@t @c [@L]: "_____See TextFormatter.java`     
 `jlogger.logfile.prefix____"@t @c [@L]: "_____See TextFormatter.java`     
 `jlogger.logfile.name______"jlog.log"_________See FileAppender.java`     
 `jlogger.logfile.kmaxsize__"100"______________See FileAppender.java`     
 `jlogger.logfile.backups___"10"_______________See FileAppender.java`

### Replacement Symbols ###

All replacement symbols begin with @ or {, per (A), (B), or (C) below.   

(A) @ followed by vararg 1..9 then a symbol. i.e. @3d = arg 3 as decimal

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

(B) Or @ followed by a symbol. LogEvent data, NOT vararg. i.e. @d = date      

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

(C) Use {} to substitute "next argument" from varargs. i.e. {} = next arg  
 
 * {} substitutes the next argument from varargs, whatever type it is.    
 * do not mix this symbol with other (@) vararg symbols in a message.   
 * however, you may mix it with other (@) non-vararg symbols.    
 * or you can mix the two if you put the arg number in braces: {1}, {2}...
 * two "{" symbols "{{" are escaped to a single left braca "{".

Please note that...    

 * If a value is null then typically it will be replaced with (null)  
 * If a value is cannot be processed then it will be replaced with ###  
 * If a symbol is corrupt then it will be replaced with ???   
 * Some symbols have a different meaning when used with an arg#  
 * Class names (m, M, c, and C) include an abbreviated package prefix   

### Notes ###

As a performance consideration, messages will not even be formatted from 
events unless they are at the unfiltered Level (i.e. Diag) of a Logger.  

If more than one appender formats an event with the same Formatter settings,
the event will be formatted only once for all of them.  

Events are written by a background thread to improve front-end performance
and to guarantee that they get flushed in the event of a crash.  

Because of this (above) the values of some logged arguments might be changed
by your app before they get logged, logging slightly stale data.  

### To Do ###

 * Create an slf4j adapter   
 * Create a Log4j2Formatter (log4j2 symbols)   
 * Write more Appenders (JSON, DB, etc.)   
 * Add some Unit Tests    
 * Roll up repeated events (Configurable)    
 * Finish coding getObjectArgumentAsString()
 * Internationalization, esp literal strings