Paoding Analysis for ElasticSearch
==================================

The Paoding Analysis plugin integrates Paoding(http://code.google.com/p/paoding/) module into elasticsearch.

In order to install the plugin, simply run: `bin/plugin -install com.thihy/elasticsearch-analysis-paoding/1.4.1.3`.

    --------------------------------------------------
    | Paoding    Analysis Plugin    | ElasticSearch  |
    --------------------------------------------------
    | master                        | master         |
    --------------------------------------------------
    | 1.4.2.1                       | 1.4.x          |
    --------------------------------------------------

The plugin includes `paoding` analyzer.

optional config `mode` can be set to `most_tokens` or `max_length`

1.install the paoding analysis plugin

2.create a index with paoding analysis config

```
curl -XPUT http://localhost:9200/thihy/ -d'
{
   "index":{
      "analysis":{
         "analyzer":{
            "paoding_1":{
               "type":"paoding",
               "mode":"max_length",
               "dict":{
               	"type":"file",
               	"file":{
               		"dir":"／path/to/dir"
               	}
               }
            }
         }
      }
   }
}'
```

Maven Usage
----------------------------------

```
<dependency>
    <groupId>com.thihy</groupId>
    <artifactId>elasticsearch-analysis-paoding</artifactId>
    <version>1.4.1.2</version>
</dependency>
```
