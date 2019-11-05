**Code for software evolution project**

Target research:
*"Understanding the evolution of Fix-inducing changes"* | *"Understanding the relation between Fix-inducing changes and software metrics"*

Target conf:
MSR | EASE

Details:
1. extracts tags from GitHub repository using Jgit
2. for each tag, extracts its commits
3. from an existing list of FICs marks commits accordingly
4. for each commit
    1. lists the files and line numbers that were changed
    2. finds the content of those files for both the current version (commit) and its previous one
5. extract metrics
    + loc
    + cyclomatic complexity
6. outputs findings (single csv with two columns for FIC and non-FIC)
    + loc
    + cyclomatic complexity
    + commit interval
7. outputs evolution findings (single csv with 4 columns): per tag frequency of FICs, frequency of non-FICs and ratio of FICs