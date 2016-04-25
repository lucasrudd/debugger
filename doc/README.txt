# debugger

## Usage

This program will read in a file and check its extension. If it is a known extension (i.e. .cpp,
.java, .clj, etc) then it will map the language type (e.g. "C++") to the file contents. If the
extension is generic or not known (i.e. .txt or .doc) then the program will read the first line(s)
of the file to look for keywords (such as "#include" "class" "import" "(ns )" etc.) to determine
the language type. If no match is found then an error will be returned and the program will close.

[I'm considering doing a clooj type application using a GUI interface and allow for the writting/pasting
of code into a text field. When the "run" button is pressed the code will be scanned and syntax errors
looked for and possible errors flagged. This is a huge undertaking though, and I'm probably going to leave
this for later]

The user should be able to choose from a list of 'symptoms' that they're experiencing, and the
code should attempt to source the cause by searching through the code. There are several symptoms the
user can choose from, each with different causes. They include
(1) Endless loops
(2) Fails to produce output
(3) Loop executes too few or too many times
(4) Illegal operations attempted (this is an interesting case, the type of illegal operation should be
                                  specified, and the operand in question should also be specified)

********* FOR CONSIDERATION ********************

Consider using unification or pattern matching to solve these problems. For example:
(let [symptom ["infinite-loop" "fails-to-print"]]
  (match symptom
    [["infinite-loop _"]]     :one cause
    [["too-few-execution" _]] :another cause
    [[_ "fails-to-print"]]    :a third cause))

********* END CONSIDERATION *********************


How to identify each problem:

(1) ENDLESS LOOP:

The program should identify all for, while, doseq, do-while, etc loops (NOTE: foreach loops should never
enter into an infinite loop so they shouldn't be an issue) in the language's specific dialect. The
line number of these loops should be identified and saved for the user (so they can manually attempt
to fix the issue) and the loop should be analyzed, depending on the type, for logical errors.
Errors include but are not limited to the following types

(1) for(int i = x, i < y, i--)    ;where x is less than y such that i is always less than y
(2) for(int i = x, i > y, i++)    ;where x is greater than y such that i is always greater than y
(3) while(x)                      ;where x always evaluates to true

(2) FAILS TO PRODUCE OUTPUT:

The program should identify all print, println, cout, etc. statements for the language in question.
If none are found then the code should alert the user, otherwise it should analyze the context in
which the print statement occurs. That is, it should attempt to find an if, for, while, cond etc.
statement in which the print statement in situated within. The code should then attempt to identify
under what conditions these statements will execute and then alert the user to the issue so that they
may further look into it themselves

(3) LOOP EXECUTES TOO FEW OR TOO MANY TIMES

Again, the program should identify all all for, while, foreach, doseq, do-while, etc loops in the 
language's specific dialect. Then, depending on the type of loop, the program should attempt to identify
various potential problems. Such problems include but are not limited to the following types:

(1) for(int i = x, i < y, i++)    ;where 'x' should be 'x + 1'
(2) for(int i = x, i < y, i++)    ;where 'i < y' should be 'i <= y'
(3) for(word : sentence)          ;where 'sentence.length()' should be 'sentence.length() + 1'
(4) while(x != y) or while(x < y) ;where 'y' should be 'y + 1' or 'x < y' should be 'x <= y' respectively


(4) ILLEGAL OPERATION ATTEMPTED:

The program will check all declaration types (int, String, char, boolean, etc.) and save the name of
the declared variable in a list of all variable names. Then, for every operation attempted (+, *, ++,
+= etc.) the program will check the things being operated upon. Finding the variable name, the program
will then search through the list of all variable names for the associated type. Any two variables that
do not match will alert the user to check that line number for more depth.

Note, there may be a situation in which the user is attempting do do something like:

int x = 0;
int y = [1, 2, 3];

x + y[0];

Ideally the program would catch instances like this, but implementing all catches of this nature would be
a huge undertaking.

### Bugs  