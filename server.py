import subprocess
import argparse
from os.path import isfile
from flask import Flask, Response, render_template, send_file
import simplejson

app = Flask(__name__)


class Config:
    jar_path = None
    sheet_path = None

    @staticmethod
    def from_json(json_path):
        config_json = simplejson.load(open(json_path, "r"))
        Config.jar_path = config_json["jar_path"]
        Config.sheet_path = config_json["sheet_path"]
        assert isfile(Config.jar_path), Config.jar_path + " jar file does not exist!"
        assert isfile(Config.sheet_path), Config.sheet_path + " sheet path does not exist!"


@app.route('/')
def home():
    process = subprocess.Popen(["scala", Config.jar_path,  "--csv", Config.sheet_path], stdout=subprocess.PIPE)
    output, error = process.communicate()
    if error:
        return Response(status=500)

    assets_json = simplejson.loads(output)
    encoded = simplejson.encoder.JSONEncoderForHTML().encode(assets_json)

    return render_template("template.html", name=encoded)


@app.route("/images/Expanded.gif")
def expanded():
    return send_file("images/Expanded.gif", mimetype='image/gif')


@app.route("/images/Collapsed.gif")
def collapsed():
    return send_file("images/Collapsed.gif", mimetype='image/gif')


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--config',  default="config.json", help="Path to config.json file")
    args = parser.parse_args()

    """Loading config"""
    Config.from_json(args.config)

    """Running the app"""
    app.run()