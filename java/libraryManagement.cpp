#include <iostream>
#include <string>
#include <sstream>
#include <cmath>
#include <algorithm>
#include <stdexcept>
#include <iomanip>

using namespace std;

class VariableNode;
class LinkedList;
class VariableManager;
class TreeNode;

class VariableNode {
public:
    string name;
    double value;
    VariableNode* next;

    VariableNode(string n, double v) : name(n), value(v), next(nullptr) {}
};

class VariableManager {
private:
    VariableNode* head;

public:
    VariableManager() : head(nullptr) {}

    void setVariable(string n, double v) {
        VariableNode* current = head;
        while (current != nullptr) {
            if (current->name == n) {
                current->value = v;
                return;
            }
            current = current->next;
        }

        VariableNode* newNode = new VariableNode(n, v);
        newNode->next = head;
        head = newNode;
    }

    double getVariable(string n, bool& found) {
        VariableNode* current = head;
        while (current != nullptr) {
            if (current->name == n) {
                found = true;
                return current->value;
            }
            current = current->next;
        }
        found = false;
        return 0.0;
    }

    void displayAll() {
        cout << "\n--- Current Variables (Linked List) ---" << endl;
        VariableNode* current = head;
        while (current != nullptr) {
            cout << "  " << current->name << " = " << fixed << setprecision(4) << current->value << endl;
            current = current->next;
        }
        if (head == nullptr) {
            cout << "  (No variables defined)" << endl;
        }
        cout << "---------------------------------------" << endl;
    }

    ~VariableManager() {
        VariableNode* current = head;
        while (current != nullptr) {
            VariableNode* next = current->next;
            delete current;
            current = next;
        }
        head = nullptr;
    }
};

template <typename T>
class Stack {
private:
    struct StackNode {
        T data;
        StackNode* next;
        StackNode(T val) : data(val), next(nullptr) {}
    };
    StackNode* top;
    int count;

public:
    Stack() : top(nullptr), count(0) {}

    void push(T val) {
        StackNode* newNode = new StackNode(val);
        newNode->next = top;
        top = newNode;
        count++;
    }

    T pop() {
        if (isEmpty()) throw runtime_error("Stack is empty");
        T poppedValue = top->data;
        StackNode* temp = top;
        top = top->next;
        delete temp;
        count--;
        return poppedValue;
    }

    T peek() const {
        if (isEmpty()) throw runtime_error("Stack is empty");
        return top->data;
    }

    bool isEmpty() const {
        return top == nullptr;
    }

    int size() const {
        return count;
    }

    ~Stack() {
        while (!isEmpty()) {
            StackNode* temp = top;
            top = top->next;
            delete temp;
        }
    }
};

class TreeNode {
public:
    string data;
    TreeNode* left;
    TreeNode* right;
    TreeNode(string val) : data(val), left(nullptr), right(nullptr) {}
};

int precedence(char op) {
    if (op == '+' || op == '-') return 1;
    if (op == '*' || op == '/') return 2;
    return 0;
}

bool isOperator(char ch) {
    return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
}

string infixToPostfix(const string& infix) {
    Stack<string> opStack;
    string postfix = "";

    for (int i = 0; i < infix.length(); ++i) {
        char ch = infix[i];
        if (ch == ' ') continue;

        if (isalnum(ch) || ch == '.') {
            string token;
            while (i < infix.length() && (isalnum(infix[i]) || infix[i] == '.')) {
                token += infix[i];
                i++;
            }
            i--;
            postfix += token + " ";
        } else if (ch == '(') {
            opStack.push("(");
        } else if (ch == ')') {
            while (!opStack.isEmpty() && opStack.peek() != "(") {
                postfix += opStack.pop() + " ";
            }
            if (!opStack.isEmpty()) opStack.pop();
        } else if (isOperator(ch)) {
            string currentOp(1, ch);
            try {
                while (!opStack.isEmpty() && opStack.peek() != "(" &&
                       precedence(opStack.peek()[0]) >= precedence(ch)) {
                    postfix += opStack.pop() + " ";
                }
            } catch (const runtime_error& e) {}
            opStack.push(currentOp);
        }
    }

    while (!opStack.isEmpty()) {
        if (opStack.peek() == "(") {
            cerr << "Error: Unmatched opening parenthesis." << endl;
            return "";
        }
        postfix += opStack.pop() + " ";
    }
    return postfix;
}

TreeNode* constructExpressionTree(const string& postfix) {
    if (postfix.empty()) return nullptr;

    Stack<TreeNode*> nodeStack;
    stringstream ss(postfix);
    string token;

    while (ss >> token) {
        if (!isOperator(token[0]) || token.length() > 1 || isalpha(token[0])) {
            nodeStack.push(new TreeNode(token));
        } else {
            if (nodeStack.size() < 2) {
                cerr << "Error: Invalid Postfix structure (Missing operands)." << endl;
                return nullptr;
            }
            TreeNode* opNode = new TreeNode(token);
            opNode->right = nodeStack.pop();
            opNode->left = nodeStack.pop();
            nodeStack.push(opNode);
        }
    }

    if (nodeStack.size() == 1) {
        return nodeStack.pop();
    }
    cerr << "Error: Malformed Postfix expression or excess operands." << endl;
    return nullptr;
}

double evaluateTree(TreeNode* root, VariableManager& varMan) {
    if (root == nullptr) return 0.0;

    if (root->left == nullptr && root->right == nullptr) {
        if (isdigit(root->data[0]) || (root->data[0] == '-' && root->data.length() > 1)) {
            try {
                return stod(root->data);
            } catch (...) {
                cerr << "Error: Invalid number format in tree." << endl;
                return NAN;
            }
        } else {
            bool found;
            double val = varMan.getVariable(root->data, found);
            if (!found) {
                cerr << "Error: Undefined variable '" << root->data << "'." << endl;
                return NAN;
            }
            return val;
        }
    }

    double val1 = evaluateTree(root->left, varMan);
    double val2 = evaluateTree(root->right, varMan);

    if (isnan(val1) || isnan(val2)) return NAN;

    if (root->data == "+") return val1 + val2;
    if (root->data == "-") return val1 - val2;
    if (root->data == "*") return val1 * val2;
    if (root->data == "/") {
        if (val2 == 0.0) {
            cerr << "Error: Division by zero." << endl;
            return NAN;
        }
        return val1 / val2;
    }
    return NAN;
}

void deleteTree(TreeNode* root) {
    if (root) {
        deleteTree(root->left);
        deleteTree(root->right);
        delete root;
    }
}

void executeCommand(const string& command, VariableManager& varMan) {
    stringstream ss(command);
    string firstWord;
    ss >> firstWord;

    if (firstWord == "set") {
        string varName, eq, valueStr;
        ss >> varName >> eq >> valueStr;
        if (eq == "=" && !varName.empty() && !valueStr.empty()) {
            try {
                double value = stod(valueStr);
                varMan.setVariable(varName, value);
                cout << varName << " set to " << value << endl;
            } catch (...) {
                cout << "Invalid value format." << endl;
            }
        } else {
            cout << "Invalid SET command format. Use: set <variable> = <value>" << endl;
        }
    } else if (firstWord == "showvars") {
        varMan.displayAll();
    } else if (firstWord == "exit") {
        // Handled in main loop
    } else {
        string postfix = infixToPostfix(command);
        if (postfix.empty()) return;

        cout << "Postfix: " << postfix << endl;

        TreeNode* root = constructExpressionTree(postfix);

        if (root) {
            double result = evaluateTree(root, varMan);

            if (!isnan(result)) {
                cout << "Result: " << fixed << setprecision(4) << result << endl;
            } else {
                cout << "Evaluation failed due to error(s) above." << endl;
            }

            deleteTree(root);
        } else {
            cout << "Error: Could not construct Expression Tree." << endl;
        }
    }
}

int main() {
    cout << "--- Advanced DSA Expression Evaluator ---" << endl;
    cout << "Key DSA: Linked List, Stack, Expression Tree." << endl;
    cout << "Commands: set <var> = <value> | <expression> | showvars | exit" << endl;

    VariableManager varManager;
    string input;

    varManager.setVariable("a", 15.0);
    varManager.setVariable("b", 3.0);

    while (true) {
        cout << "\n> ";
        getline(cin, input);

        if (input == "exit" || cin.eof()) {
            break;
        }
        if (!input.empty()) {
            executeCommand(input, varManager);
        }
    }

    return 0;
}