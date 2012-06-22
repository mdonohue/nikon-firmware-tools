package com.nikonhacker.dfr;

import java.util.EnumSet;
import java.util.Set;

/** output options */
public enum OutputOption {
    REGISTER    ("register",        "use AC, FP, SP instead of R13, R14, R15", false),
    DMOV        ("dmov",            "use LD/ST for some DMOV operations", false),
    SHIFT       ("shift",           "use LSR, LSL, ASR instead of SR2, LSL2, ASR2 (adding 16 to shift)", false),
    STACK       ("stack",           "use PUSH/POP for stack operations", false),
    SPECIALS    ("specials",        "use AND, OR, ST, ADD instead of ANDCCR, ORCCR, STILM, ADDSP", false),

    CSTYLE      ("cstyle",          "use C style operand syntax", false),
    DOLLAR      ("dollar",          "use $0 syntax for hexadecimal numbers", false),

    ADDRESS     ("address",         "include memory address", true),
    OFFSET      ("offset",          "include file position (add offset)", false),
    HEXCODE     ("hexcode",         "include hex version of instruction and operands", true),
    BLANKS      ("blanks",          "include a large blank area before disassembled instruction", true),

    STRUCTURE   ("structure",       "structural code analysis (code flow, symbols, etc). Needs more resources.", true),
    ORDINAL     ("ordinalnames",    "(if structure is enabled) generate names based on ordinal numbers instead of address", false),
    PARAMETERS  ("parameters",      "(if structure is enabled) try to resolve not only functions but also parameters", false),
    INT40       ("int40",           "(if structure is enabled) resolve calls through INT40 wrapper", true),

    //FILEMAP     ("filemap",         "write file map"),
    //MEMORYMAP   ("memorymap",       "write memory map"),

    //SYMBOLS     ("symbols",         "write symbol table"),
    //XREF1       ("crossreference",  "write cross reference"),
    //XREF2       ("xreference",      "write cross reference"),

    VERBOSE     ("verbose",         "verbose messages", false),
    DEBUG       ("debug",           "debug disassembler", false)
    ;
    private String key;
    private String help;
    private boolean defaultValue;

    public static EnumSet<OutputOption> formatOptions = EnumSet.of(REGISTER, DMOV, SHIFT, STACK, SPECIALS, CSTYLE, DOLLAR, ADDRESS, OFFSET, HEXCODE, BLANKS);

    OutputOption(String key, String help, boolean defaultValue) {
        this.key = key;
        this.help = help;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getHelp() {
        return help;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public static String getFullHelp(Character option) {
        String s= "Here are the allowed output options" + (option==null?"":" (-" + option + ") :\n");
        for (OutputOption outputOption : EnumSet.allOf(OutputOption.class)) {
            s += (option==null?"  ":("  -" + option)) + outputOption.key + " : " + outputOption.help + "\n";
        }
        return s;
    }

    public static OutputOption getByKey(String key) {
        for (OutputOption outputOption : EnumSet.allOf(OutputOption.class)) {
            if (outputOption.key.equals(key)) return outputOption;
        }
        return null;
    }

    public static void setOption(Set<OutputOption> outputOptions, String key, boolean value) throws ParsingException {
        OutputOption option = getByKey(key);
        if (option == null) {
            throw new ParsingException("Unrecognized output option '" + key + "'");
        }
        else {
            if (value) {
                outputOptions.add(option);
            }
            else {
                outputOptions.remove(option);
            }
        }
    }

    public static boolean parseFlag(Set<OutputOption> outputOptions, Character option, String optionValue) throws ParsingException {
        if ("?".equals(optionValue)) {
            System.err.println(getFullHelp(option));
            return false;
        }

        boolean value = true;
        String key = optionValue;
        if (key.toLowerCase().startsWith("no")) {
            value = false;
            key = key.substring(2);
        }

        setOption(outputOptions, key, value);

        return true;
    }

}
