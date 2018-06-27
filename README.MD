  
<h2> Usage Instructions </h2>  
<p>Open terminal in project directory</p>
<p>Ensure java JDK is installed</p>
<p>npm install -g @angular/cli if not installed</p>
<p>Execute searcher.bat (build index) THEN searcher-front-end.bat (start front end)</p>
<p><strong>OR</strong><p>
<p>Navigate to Lucene directory, execute java -jar target/Lucene-0.0.1-SNAPSHOT.jar to create index</p>
<p>Navigate to lucene-searcher/lucene-searcher directory, execute ./mvnw package to create Spring Boot Executable. Then execute java -jar target/lucene-searcher-0.0.1-SNAPSHOT.jar to start Spring Boot process </p>
<p>Navigate to lucene-searcher/lucene-searcher-front-end and execute ng serve --open to open angular front-end interface and begin searching tweets!</p>  

<h2>Twitter Search Engine in Action</h2>

<p>This is the web front-end interface for our Twitter Search Engine. There is an input bar that the user enters their query into. They can press search which searches all tweets in the index or they can search the tweets that are nearest to them.</p>

