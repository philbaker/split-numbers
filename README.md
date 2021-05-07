# Splitting numbers

A function that splits a number into its component base elements.

For example, passing in `467` would return an array as follows:

```
[400 60 7]
```

Some other test cases are:

```
39 => [30 9],
100 => [100 0 0],
-321 => [-300 -20 -1]
```

## Things to consider
There are some things that it might be worth considering:

- What happens if the number passed isn't an integer?
- What happens if the number passed isn't a number?
- Is your method efficient? Could it be slow if the number was really big?

## Running from command line
`lein run 467`
