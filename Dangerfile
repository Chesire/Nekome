# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# AndroidLint
android_lint.skip_gradle_task = true
android_lint.report_file = 'app/build/reports/lint-results-debug.xml'
android_lint.gradle_task = "lintDebug"
android_lint.lint(inline_mode: true)

# CheckstyleFormat
checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'app/build/reports/detekt/detekt.xml'