# Web Engineering 2024-2025 / Lab 1: Git Race

Welcome to the first lab assignment of the 2024-2025 course!
This guide will help you complete the assignment efficiently.
Although this guide is command-line oriented, you are welcome to use IDEs like **VS Code**, **IntelliJ IDEA**, or **Eclipse**—all of which fully support the tools we'll be using. 
Ensure you have at least **Java 17** installed on your system before getting started.

## System Requirements

For this assignment, we'll be using the following technologies:

1. **Programming Language**: We’re using [Kotlin 2.0.20](https://kotlinlang.org/), a versatile, open-source language that’s popular for Android development and server-side applications.

2. **Build System**: The project is built using [Gradle 8.5](https://gradle.org/), a powerful tool that automates the building process, including compiling, linking, and packaging your code.

3. **Framework**: The application is based on [Spring Boot 3.3.3](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/), a framework that simplifies the development of production-grade applications. It requires Java 17 and is compatible with Java up to version 21.

## Objective

Your goal is to enhance and document the existing `Hello World MVC application by introducing new functionalities.
Specifically, you should:

- **Add at least 100 lines** of documentation and code improvements.
- Document your work in the source code and provide explanations in the [description.md](../description.md) file.
- Follow [Kotlin best practices](https://kotlinlang.org/docs/kotlin-doc.html) for Kotlin files and [GitHub Markdown](https://guides.github.com/features/mastering-markdown/) for `.md` files.

### Potential New Functionalities

Consider adding features in the `HelloController` such as:

- **Multi-language Support**: Allow users to select their preferred language (e.g., English, Spanish, French).
- **Greeting History**: Log and display the history of greetings, showing the user’s name and the time the greeting was generated.
- **REST API Endpoint**: Provide a RESTful API that returns a greeting message.
- **Time-based Greeting**: Modify the greeting message based on the time of day (e.g., “Good Morning”, “Good Afternoon”, “Good Evening”).
- **User Authentication**: Implement basic user authentication to allow users to log in and receive a personalized greeting.
- **Theme Customization**: Allow users to customize the look and feel of the application, such as changing the background color or font style.

### What to Document

Your documentation should cover topics such as:

- How to build the code.
- How to test the code.
- How to deploy the code.
- The technologies used in the code and how they work.
- Explanations of specific code segments.
- The purpose of specific Java annotations.

The documentation must be in English and should meet at least a B1 level of proficiency.

### Bonus Opportunity

Outstanding contributions will earn you a 5% bonus on your individual project score  for the _URLShortener_ project. 
Contributions are considered outstanding if they:

- Can be used as reference documentation.
- Include new, working, and tested code (following [Test-Driven Development (TDD)](https://en.wikipedia.org/wiki/Test-driven_development) practices).
- Demonstrate advanced use of tools like _git_, _Gradle_, or _GitHub Actions_.

## Submission and Evaluation

### Submission Deadline

The deadline for this assignment is **September 27th**. 
If you fail to submit by then, you will incur a 20% penalty on your individual project score for the _URLShortener_ project. 
This penalty only applies if you have not attempted to submit anything of value.

### Evaluation with GitHub Education

Your work will be evaluated with the support of GitHub Education, which means the following:

- **Commit History**: Your GitHub commit history will be reviewed to assess your contributions over time. Regular, meaningful commits are encouraged.
- **Pull Requests**: If applicable, you may need to submit your changes through pull requests. 

If you have any questions, don’t hesitate to ask by posting an [Issue](https://github.com/UNIZAR-30246-WebEngineering/lab1-git-race/issues) on GitHub.

## Getting Started

### Setting Up Git

1. **Install Git**: Download and install [git](https://git-scm.com/) on your system.

2. **GitHub Account**: If you haven’t already, sign up for an education account on [GitHub](https://github.com) using your real name and university email address.

3. **Configure Git**: Set up Git on your local machine with the following commands:

   ```bash
   git config --global user.name "Your Real Name"
   git config --global user.email "your_nip@unizar.es"
   ```   

4. **Authenticate with GitHub**: : Authenticate via HTTPS (recommended) or SSH. You can follow the [GitHub guide](https://docs.github.com/en/get-started/quickstart/set-up-git) for detailed instructions.

### Clone and Run the Application

1. **Clone the Repository**: Use the following commands to clone the repository to your local machine:

   ```bash
   git clone https://github.com/your-github-username/lab1-git-race-your-github-username
   cd lab1-git-race-your-github-username
   ```

2. **Test the Repository**: Navigate to the `lab1-git-race-your-github-username` folder in your terminal and run:

   ```bash
   ./gradlew check
   ```

3. **Run the Application**: Start the application with:

   ```bash
   ./gradlew bootRun
   ```
   
   and open your browser at [http://localhost:8080](http://localhost:8080) to see the application running.

### Complete the assignment

1. **Review and Document**: Go through the code and add relevant documentation. Make sure your additions are correct and useful.
2. **Test Your Changes**: After updating the documentation, ensure everything works by running `./gradlew check` again.
3. **Push Your Changes**: If all tests pass, push your changes to GitHub with:

   ```bash
   git push
   ```

Good luck with your assignment!
