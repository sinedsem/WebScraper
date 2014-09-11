WebScraper
==========

Test task for HireRight

Assume that the sentence is a set of words from dot to dot or from tag to tag.
Application do not support css or js, otherwise it take a long months to 
write a mini-browser (using only clear java!)
 
The only exception is <a> - it's quite common in the center of a sentence.

Words are searched either by exact match, or with the ending (s|es)

## Launching

java -jar webscraper.jar \<url\> \<word(s)\> \<-c\> \<-w\> \<-e\> \<-v\>

- (required) **url**: url or path to file with urls' list
- (required) **word(s)**: word or list of words with “,” delimiter
- **params**: word or list of words with “,” delimiter
- **-c**: count number of characters of each web page
- **-w**: count number of word(s) occurrence of each web page
- **-e**: extract sentences which contain given words
- **-v**: information about time spent