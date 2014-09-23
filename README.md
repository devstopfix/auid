Application Unique Identifier (AUID)
====================================

This Clojure library generates unique 64-bit identifiers outside of your database for use as [surrogate keys][1]. It is intended for use in testing and prototypes - NOT for long term storage of production data (see the constraints).

AUID vs Snowflake
-----------------

It is based on the [Snowflake][2] library from [Twitter][3] with some important differences:

1. Snowflake uses a 41 bit millisecond count from an epoch - this is good for ~69 years. AUID uses 40 bits from an epoch of your choice which is good for [35 years from now][4]. If no epoch is given then the [Clojure 1.0 announcement date][5] is used.
2. Snowflake protects from non-monotonic clocks (i.e. clocks that run backwards) whereas AUID does not
3. AUID only allows 4 bits as a machine identifier (16 machines) whereas Snowflake uses 10 bits (1024 machines)
4. Snowflake requires provisioning as a service - AUID is embedded in your application as a library.

Usage
=====

``` clojure
(require '[devstopfix.auid :as auid])

(def next-id (auid/next-id-fn 7))
(next-id)
;2895983927559923481
```

API
---

The ```auid/next-id-fn``` is normally called with a single numeric parameter, of which the bottom 4 bits (0-15) are used to identify the machine or application that is generating the numbers. It returns a function that is called with no arguments to generate ids.


To Do
=====

1. The identifier is currently 63 bits in length, though this is not noticeable unless you are creating IDs at a time well past the epoch. I need to investigate passing a signed Java Long to a PostgreSQL int64 and keep it unsigned to avoid negative keys in the database

[1]: http://en.wikipedia.org/wiki/Surrogate_key
[2]: https://github.com/twitter/snowflake/releases/tag/snowflake-2010
[3]: https://blog.twitter.com/engineering
[4]: http://www.wolframalpha.com/input/?i=2%5E40ms+from+now
[5]: https://groups.google.com/forum/#!topic/clojure/HmYdFr2RDd0
