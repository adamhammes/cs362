from jinja2 import Environment, FileSystemLoader
import yaml


def make_title(title):
    normalized = title.lower().replace(' ', '-')
    return 'output/' + normalized + '.tex'


def write_files(template_path, data_path):
    with open(data_path, 'r') as f:
        data = yaml.safe_load(f)

    env = Environment(loader=FileSystemLoader('./'))
    template = env.get_template(template_path)

    for author, cases in data.items():
        for case in cases:
            with open(make_title(case), 'w') as f:
                to_write = template.render(author=author, title=case)
                f.write(template.render(author=author, title=case))


def main():
    write_files('elaboration.jnj', 'hw_assignments.yaml')

if __name__ == '__main__':
    main()

