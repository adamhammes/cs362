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
    print(template)

    for author, cases in data.items():
        for case in cases:
            print(make_title(case))
            with open(make_title(case), 'w') as f:
                to_write = template.render(author=author, title=case)
                print(to_write)
                f.write(template.render(author=author, title=case))


def main():
    template_path = sys.argv[1]
    write_files(template_path, 'hw_assignments.yaml')

if __name__ == '__main__':
    main()

