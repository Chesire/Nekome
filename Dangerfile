# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# General
failure "Please provide a summary in the Pull Request description" if github.pr_body.length < 5
warn "This PR does not have any assignees yet." unless github.pr_json["assignee"]
can_merge = github.pr_json["mergeable"]
warn("This PR cannot be merged yet.", sticky: false) unless can_merge
#github.dismiss_out_of_range_messages

# AndroidLint
lint_dir = "**/reports/lint-results*.xml"
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.filtering = true
  android_lint.report_file = file_name
  android_lint.lint(inline_mode: true)
end

# CheckstyleFormat
checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'build/reports/detekt/detekt.xml'

# JUnit
junit_tests_dir = "**/test-results/**/*.xml"
Dir[junit_tests_dir].each do |file_name|
  junit.parse file_name
  junit.show_skipped_tests = true
  junit.report
end