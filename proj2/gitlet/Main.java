package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zechuan Wang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     *  java gitlet.Main init
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                checkGitletInitialized();
                validateNumArgs(args, 1);
                Repository.log();
                break;
            case "global-log":
                checkGitletInitialized();
                validateNumArgs(args, 1);
                Repository.global_log();
                break;
            case "find":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                checkGitletInitialized();
                validateNumArgs(args, 1);
                Repository.status();
                break;
            case "checkout":
                checkGitletInitialized();
                Repository.checkout(args);
                break;
            case "branch":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                checkGitletInitialized();
                validateNumArgs(args, 2);
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkGitletInitialized() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

}
