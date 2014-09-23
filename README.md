Application Unique Identifier (AUID)
====================================

This Clojure library generates unique 64-bit identifiers outside of your database for use as [surrogate keys][1]. It is intended for use in testing and prototypes - NOT for long term storage of production data (see the constraints).

AUID vs Snowflake
-----------------

It is based on the [Snowflake][2] library from [Twitter][3] with some important differences:
1. Snowflake uses a 41 bit millisecond count from an epoch - this is good for ~69 years. AUID uses 40 bits from an epoch of your choice which is good for [35 years from now][4]
2. Snowflake puts the timestamp first to make the IDs roughly time-ordered - AUID puts the counter factor first to make the higher bits distinct and therefore distinguishable to the eye in a table
3. Snowflake protects from non-monotonic clocks (i.e. clocks that run backwards) whereas AUID does not
4. AUID only allows 4 bits as a machine identifier (16 machines) whereas Snowflake uses 10 bits (1024 machines)
5. Snowflake requires provisioning as a service - AUID is embedded in your application as a library.







[1]: http://en.wikipedia.org/wiki/Surrogate_key
[2]: https://github.com/twitter/snowflake/releases/tag/snowflake-2010
[3]: https://blog.twitter.com/engineering
[4]: http://www.wolframalpha.com/input/?i=2%5E40ms+from+now
