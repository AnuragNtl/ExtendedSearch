# ExtendedSearch
<h3>A Java based tool that demonstrates filtering of items in a list based on wikipedia knowledge(Wikipedia API)</h3>
<p>This is a demonstration of what the android app does.</p>
For example run:
<pre>
cd bin
java ExtendedSearch
</pre>
<p>
It shows a list of movies.You can also drag and drop your own list here from files or text.
<br>
Search which of these movies are Martin Scorsese directed?
</p>

<img src="sampleScr.jpg" style="width:200;height:200"></img>
<p>It uses wikipedia API to extract information about every item in the list.</p>
<p>You can modify how searching is done in every response by implementing locations.InfoSearcher interface.
<br>
You can add your own target API other than Wikipedia by extending locations.Locations class.
</p>
