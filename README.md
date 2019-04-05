# juan
Basic program simulating "john the ripper"

 Usage: `java -jar juan.jar`

    -h, --help
    -s, --show_config
    -n, --number 'NUM'
    -t, --type_hash
    -m, --mode
    -i, --incremental
    -p, --print_interval 'NUM'
    -v, --verbose
    -w, --write_to_file
    -f, --file '/path/to/input/file'
               ` echo <passwd_plain_text> | tr -d '\n' | sha256sum | cut -f 1 -d ' ' `   # only bash terminal
         
